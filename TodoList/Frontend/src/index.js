'use strict'

import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'webpack-jquery-ui/css'
import {todo} from './todo'
import {note} from "./note"
import {auth} from './auth'
import {Amplify} from "aws-amplify"

const oauth = {
    domain: 'td-frontend.auth.eu-west-1.amazoncognito.com',
    scope: ['email'],
    redirectSignIn: 'https://s3-eu-west-1.amazonaws.com/td-frontend/index.html',
    redirectSignOut: 'https://s3-eu-west-1.amazonaws.com/td-frontend/index.html',
    responseType: 'token'
}

Amplify.configure({
    Auth: {
        region: 'eu-west-1',
        userPoolId:'userPoolId',
        userPoolWebClientId: 'webClientId',
        identityPoolId: 'identityPoolId',
        mandatorySignIn: false,
        oauth: oauth
    },
    Storage: {
        bucket: 'td-str',
        region: 'eu-west-1',
        identityPoolId: 'identityPoolId',
        level: 'public'
    }
})

$(function () {
    auth.activate().then((user) => {
        if (user) {
            todo.activate(auth)
            note.activate(auth)
        }
    })

})
