'use strict'

import $ from 'jquery'
import 'webpack-jquery-ui/datepicker'
import {addTpl, editTpl, errTpl, navBarTpl, todoListTpl} from './templates'

const view = {renderList, renderAddButton, renderEditArea, renderError, renderLink, renderNote}
export {view}

function renderList(body) {
    $('#content').html(todoListTpl(body))
}

function renderAddButton() {
    $('#edit-area').html(addTpl())
}

function renderEditArea(id) {
    $('#edit-area').html(editTpl())
    $('#todo-duedate').datepicker()
    setTimeout(function () {
        $('#edit-area').html(editTpl())
        $('#todo-duedate').datepicker()
        if (id) {
            $('#todo-id').val(id)
            $('#todo-duedate').val($('#' + id + ' #due-date').text())
            $('#todo-action').val($('#' + id + ' #action').text())
            $('#todo-stat').val($('#' + id + '#stat').val())
            $('#todo-note').val($('#' + id + ' #note').text())
        }
    }, 100)
}


function renderError(body) {
    $('#error').html(errTpl(body.err))
}

function renderLink(isAuth) {
    $('#navbarNav').html(navBarTpl(isAuth))
}

function renderNote(text) {
    $('#todo-note').text(text)
}
