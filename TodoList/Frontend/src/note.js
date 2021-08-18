'use strict'

import $ from 'jquery'
import {Storage} from "aws-amplify"
import {AudioControl} from './audio/control'
import {v4 as uuidv4}  from 'uuid'
import {view} from "./view"

const note = {activate, bindRecord}
export {note}
const NOTE_API = 'https://62yafk0kng.execute-api.eu-west-1.amazonaws.com/test/note/'
const STORAGE_BUCKET = 'https://s3-eu-west-1.amazonaws.com/td-str/public/'

let auth
let ac
let itv

function activate(authObj) {
    auth = authObj
}

function bindRecord() {
    $('#todo-note-start').unbind('click')
    $('#todo-note-start').on('click', e => {
        startRecord()
    })

    $('#todo-note-stop').unbind('click')
    $('#todo-note-stop').on('click', e => {
        stopRecord()
    })

}

function startRecord() {
    view.renderNote('Speak')
    ac = AudioControl({checkAudioSupport: false})
    ac.supportsAudio((supported) => {
        if (supported) {
            ac.startRecording()
        } else {
            alert('No audio support!')
        }
    })
}

function stopRecord() {
    const noteId = uuidv4()

    view.renderNote('Thinking')
    ac.stopRecording()
    ac.exportWav((blob, recordSampleRate) => {
        Storage.put(noteId + '.wav', blob)
            .then(() => {
                submitNote(noteId, recordSampleRate)
            })
            .catch(err => {
                console.log(err)
            })
        ac.close()
    })
}

function submitNote(noteId, recordedSampleRate) {
    const body = {
        noteLang: 'en-US',
        noteUri: STORAGE_BUCKET + noteId + '.wav',
        noteFormat: 'wav',
        noteName: noteId,
        noteSampleRate: recordedSampleRate
    }


    auth.session().then(session => {
        $.ajax(NOTE_API, {
            data: JSON.stringify(body),
            contentType: 'application/json',
            type: 'POST',
            headers: {
                Authorization: session.idToken.jwtToken
            },
            success: function (body) {
                if (body.length > 0) {
                    pollNote(noteId)
                } else {
                    $('#error').html(body.err)
                }
            }
        })
    }).catch(err => view.renderError(err))
}

function pollNote(noteId) {
    let count = 0
    itv = setInterval(() => {
        auth.session().then(session => {
            $.ajax(NOTE_API + noteId, {
                type: 'GET',
                headers: {
                    Authorization: session.idToken.jwtToken
                },
                success: function (body) {
                    if (body.status === 'COMPLETED') {
                        clearInterval(itv)
                        view.renderNote(body.transcripts[0].transcript)
                    } else if (body.status === 'FAILED') {
                        clearInterval(itv)
                        view.renderNote('FAILED')
                    } else {
                        count++
                        let dots = ''
                        for (let idx = 0; idx < count; idx++) {
                            dots = dots + '.'
                        }
                        view.renderNote('Still thinking' + dots)
                    }
                }
            })
        }).catch(err => view.renderError(err))
    }, 3000)
}
