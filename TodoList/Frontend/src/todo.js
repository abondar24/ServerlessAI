'use strict'

import $ from 'jquery'
import {view} from './view'

const todo = {activate}
export {todo}

const API_URL = `https://eee7kn9pel.execute-api.eu-west-1.amazonaws.com/test/todo/`
let auth


function activate(authObj) {
    auth = authObj
    list(() => {
        bindList()
        bindEdit()
    })
    $('#content').bind('DOMSubtreeModified', () => {
        bindList()
        bindEdit()
    })
}

function list(cb) {
    auth.session().then(session => {
        $.ajax(API_URL, {
            type: 'GET',
            headers: {
                Authorization: session.idToken.jwtToken
            },
            success: function (body) {
                if (body.length >0) {
                    view.renderList(body)
                } else {
                    view.renderError(body)
                }
                cb && cb()
            },
            fail: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus)
                alert(errorThrown)
            }
        })
    }).catch(err => view.renderError(err))

}

function bindList() {
    $('.todo-item-edit').unbind('click')
    $('.todo-item-edit').on('click', (e) => {
        view.renderEditArea(e.currentTarget.id)
    })
    $('.todo-item-delete').unbind('click')
    $('.todo-item-delete').on('click', (e) => {
        del(e.currentTarget.id)
    })
}

function del(id) {
    auth.session().then(session => {
        $.ajax(API_URL + id, {
            type: 'DELETE',
            headers: {
                Authorization: session.idToken.jwtToken
            },
            success: function (body) {
                if (body.Action === 'Item Deleted') {
                    list()
                } else {
                    $('#error').html(body.err)
                }
            }
        })
    }).catch(err => view.renderError(err))

}

function bindEdit() {
    $('#input-todo').unbind('click')
    $('#input-todo').on('click', e => {
        e.preventDefault()
        view.renderEditArea()
    })

    $('#todo-save').unbind('click')
    $('#todo-save').on('click', e => {
        e.preventDefault()
        if ($('#todo-id').val().length > 0) {
            update(() => {
                view.renderAddButton()
            })
        } else {
            create(()=>{
                view.renderAddButton()
            })
        }
            })

    $('#todo-cancel').unbind('click')
    $('#todo-cancel').on('click',e=>{
        e.preventDefault()
        view.renderAddButton()
    })
}

function create(cb){
    auth.session().then(session => {
        $.ajax(API_URL,{
            data: JSON.stringify(gather()),
            contentType: 'application/json',
            type: 'POST',
            headers: {
                Authorization: session.idToken.jwtToken
            },
            success: function (body){
                if (body.id){
                    list(cb)
                } else {
                    $('#error').html(body.err)
                    cb && cb()
                }
            }
        })
    }).catch(err => view.renderError(err))

}

function update(cb){
    auth.session().then(session => {
        $.ajax(API_URL,{
            data: JSON.stringify(gather()),
            contentType: 'application/json',
            type: 'PUT',
            headers: {
                Authorization: session.idToken.jwtToken
            },
            success: function (body){
                if (body.id){
                    list(cb)
                } else {
                    $('#error').html(body.err)
                    cb && cb()
                }
            }
        })
    }).catch(err => view.renderError(err))

}

function gather(){
    return {
        id: $('#todo-id').val(),
        dueDate: $('#todo-duedate').val(),
        action: $('#todo-action').val(),
        stat: $('#todo-stat').is(':checked') ,
        note: $('#todo-note').val()
    }
}
