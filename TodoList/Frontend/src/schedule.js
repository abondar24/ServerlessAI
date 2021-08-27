'use strict'

import $ from 'jquery'
import {view} from "./view"

const schedule = {activate}
export {schedule}

const SCHEDULE_API = 'https://uopmlnigx9.execute-api.eu-west-1.amazonaws.com/test/schedule/day/'
let itv
let auth

function activate(authObj){
    auth = authObj
    view.renderScheduleButton()
    bindButton()
}

function bindButton(){
    $('#todo-schedule').unbind('click')
    $('#todo-schedule').on('click',e=>{
        bindSchedule()
    })
}


function bindSchedule(){
    auth.session().then(session =>{
        $.ajax(SCHEDULE_API,{
            contentType: 'application/json',
            type: 'PUT',
            headers:{
                Authorization: session.idToken.jwtToken
            },
            success: function (body){
                if (body.taskStatus === 'scheduled'){
                    pollSchedule(body.taskId)
                } else {
                    $('#error').innerHTML = body
                }
            }
        })
    }).catch(err=> view.renderError(err))
}
function pollSchedule(taskId){
    itv = setInterval(()=>{
        auth.session().then(session =>{
            $.ajax(SCHEDULE_API+taskId,{
                contentType: 'application/json',
                type: 'GET',
                headers: {
                    Authorization: session.idToken.jwtToken
                },
                success: function (body){
                    if (body.taskStatus === 'completed'){
                        clearInterval(itv)
                        playSchedule(body.signedUrl)
                    }
                    if (body.taskStatus === 'failed'){
                        clearInterval(itv)
                        $('#error').innerHTML = 'ERROR: '+ body
                    }
                }
            })
        }).catch(err => view.renderError(err))
    },3000)
}

function playSchedule(url){
    let audio = document.createElement('audio')
    audio.src = url
    audio.play()
}
