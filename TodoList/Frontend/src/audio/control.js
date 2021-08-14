'use strict'

import {AudioRecorder} from './recorder'

export {AudioControl}


function AudioControl(options) {
    let recorder
    let audioRecorder
    let checkAudioSupport
    let audioSupported
    let playbackSource
    let UNSUPPORTED = 'Audio is not supported'
    options = options || {}
    checkAudioSupport = options.checkAudioSupport !== false

    function startRecording(onSilence, visualizer, silenceDetectionConfig) {
        onSilence = onSilence || function () {}
        visualizer = visualizer || function () {}

        isAudioSupported()

        recorder = audioRecorder.createRecorder(silenceDetectionConfig)
        recorder.record(onSilence, visualizer)
    }

    function stopRecording() {
        isAudioSupported()

        recorder.stop()
    }

    function exportWav(callback, sampleRate) {
        isAudioSupported()

        if (!(callback && typeof callback === 'function')) {
            throw new Error('Callback function is not passed to export')
        }

        sampleRate = (typeof sampleRate !== 'undefined') ? sampleRate : 16000
        recorder.exportWav(callback, sampleRate)
        recorder.clear()
    }

    function playHtmlAudioElement(buffer, callback) {
        if (typeof buffer === 'undefined') {
            return
        }

        let myBlob = new Blob([buffer]);
        let audio = document.createElement('audio');
        let objectUrl = window.URL.createObjectURL(myBlob);
        audio.src = objectUrl
        audio.addEventListener('ended', function () {
            audio.currentTime = 0
            if (typeof callback === 'function') {
                callback()
            }
        })
        audio.play()
    }

    function play(buffer, callback) {
        if (typeof buffer === 'undefined') {
            return
        }

        let myBlob = new Blob([buffer]);
        let fileReader = new FileReader()
        fileReader.onload = function () {
            playbackSource = audioRecorder.audioCoontext().createBufferSource()
            audioRecorder.audioCoontext().decodeAudioData(this.result, function (buf) {
                playbackSource.buffer = buf
                playbackSource.connect(audioRecorder.audioContext().destination)
                playbackSource.onended = function (event) {
                    if (typeof callback === 'function') {
                        callback()
                    }
                }
                playbackSource.start(0)
            })
        }
        fileReader.readAsArrayBuffer(myBlob)
    }

    function stop() {
        if (typeof playbackSource === 'undefined') {
            return
        }
        playbackSource.stop()
    }

    function clear() {
        recorder.clear()
    }

    function supportsAudio(callback) {
        callback = callback || function () {}
        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
            audioRecorder = AudioRecorder()
            audioRecorder.requestDevice()
                .then(() => {
                    audioSupported = true
                    callback(audioSupported)
                })
                .catch(() => {
                    audioSupported = false
                    callback(audioSupported)
                })
        } else {
            audioSupported = false
            callback(audioSupported)
        }
    }

    function close() {
        audioRecorder.close()
    }

    if (checkAudioSupport) {
        supportsAudio()
    }

    function isAudioSupported() {
        audioSupported = audioSupported !== false
        if (!audioSupported) {
            throw new Error(UNSUPPORTED)
        }
    }

    return {
        startRecording,
        stopRecording,
        exportWav,
        play,
        stop,
        clear,
        playHtmlAudioElement,
        supportsAudio,
        close
    }
}
