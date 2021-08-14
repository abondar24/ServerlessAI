'use strict'

export {AudioRecorder}

function Recorder(source, silenceDetectionConfig) {
    let worker = new Worker('./worker.js')

    silenceDetectionConfig = silenceDetectionConfig || {}
    silenceDetectionConfig.time = silenceDetectionConfig
        .hasOwnProperty('time') ? silenceDetectionConfig.time : 1500

    silenceDetectionConfig.amplitude = silenceDetectionConfig
        .hasOwnProperty('amplitude') ? silenceDetectionConfig.amplitude : 0.2

    let recording = false
    let currCallback
    let start
    let silenceCallback
    let visualisationCallback
    let node = source.context.createScriptProcessor(4096, 1, 1)

    worker.onmessage = function (message) {
        let blob = message.data
        currCallback(blob, source.context.sampleRate)
    }

    worker.postMessage({
        command: 'init',
        config: {
            sampleRate: source.context.sampleRate
        }
    })

    function record(onSilence, visualizer) {
        silenceCallback = onSilence
        visualisationCallback = visualizer
        start = Date.now()
        recording = true
    }

    function stop() {
        recording = false
    }

    function clear() {
        stop()
        worker.postMessage({command: 'clear'})
    }

    function exportWav(callback, sampleRate) {
        currCallback = callback
        worker.postMessage({
            command: 'export',
            sampleRate: sampleRate
        })
    }


    let analyser = source.context.createAnalyser()
    analyser.minDecibels = -90
    analyser.maxDecibels = -10
    analyser.smoothingTimeConstant = 0.85

    function analyse() {
        analyser.fftSize = 2048

        let bufferLength = analyser.fftSize
        let dataArray = new Uint8Array(bufferLength)
        let amplitude = silenceDetectionConfig.amplitude
        let time = silenceDetectionConfig.time

        analyser.getByteTimeDomainData(dataArray)

        if (typeof visualisationCallback === 'function') {
            visualisationCallback(dataArray, bufferLength)
        }

        for (let i = 0; i < bufferLength; i++) {
            let currValueTime = (dataArray[i] / 128) - 1.0
            if (currValueTime > amplitude || currValueTime < (-1 * amplitude)) {
                start = Date.now()
            }
        }

        let newTime = Date.now()
        let elapsedTime = newTime - start
        if (elapsedTime > time) {
            silenceCallback()
        }
    }

    function close() {
        worker.postMessage({command: 'close'})
    }

    node.onaudioprocess = function (audioProcessingEvent) {
        if (!recording) {
            return
        }

        worker.postMessage({
            command: 'record',
            buffer: [audioProcessingEvent.inputBuffer.getChannelData(0)]
        })
        analyse()
    }

    source.connect(analyser)
    analyser.connect(node)
    node.connect(source.context.destination)

    return {
        record,
        stop,
        clear,
        close,
        exportWav
    }
}

function AudioRecorder() {
    let audioCtx
    let audioStream
    let rec

    function requestDevice() {
        if (typeof audioCtx === 'undefined') {
            window.AudioContext = window.AudioContext || window.webkitAudioContext
            audioCtx = new AudioContext()
        }

        return navigator.mediaDevices.getUserMedia({audio: true})
            .then(function (stream) {
                audioStream = stream
            })
    }

    function createRecorder(silenceDetectionConfig) {
        rec = Recorder(audioCtx.createMediaStreamSource(audioStream), silenceDetectionConfig)
        return rec
    }

    function audioContext() {
        return audioCtx
    }

    function close() {
        rec.close()
        audioCtx.close()
    }

    return {
        requestDevice,
        createRecorder,
        audioContext,
        close
    }
}
