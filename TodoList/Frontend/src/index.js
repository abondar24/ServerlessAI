'use strict'

import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'webpack-jquery-ui/css'
import {todo} from './todo'
import {auth} from './auth'
import {Amplify} from "aws-amplify"

const oauth = {
    domain: 'td-frontend.auth.eu-west-1.amazoncognito.com',
    scope: ['email'],
    redirectSignIn: `https://td-frontend.s3-website-eu-west-1.amazonaws.com/index.html`,
    redirectSignOut: `https://td-frontend.s3-website-eu-west-1.amazonaws.com/index.html`,
    responseType: 'token'
}

Amplify.configure({
    Auth:{
        region: 'eu-west-1',
        userPoolId:'userPoolId',
        userPoolWebClientId: 'webClientId',
        identityPoolId: 'identityPoolId',
        mandatorySignIn: false,
        oauth: oauth
    }
})

$(function (){
    auth.activate().then((user) =>{
        if (user){
            todo.activate(auth)
        }
    })

})
