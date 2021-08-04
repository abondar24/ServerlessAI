'use strict'

import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'webpack-jquery-ui/css'
import {todo} from './todo'
import {auth} from './auth'
import {Amplify} from "aws-amplify"

const oauth = {
    domain: "//TODO add cognito domain",
    scope: ['email'],
    redirectSignIn: `https://td-frontend.s3-website-eu-west-1.amazonaws.com/index.html`,
    redirectSignOut: `https://td-frontend.s3-website-eu-west-1.amazonaws.com/index.html`,
}

Amplify.configure({
    Auth:{
        region: 'eu-west-1',
        userPoolId:'TODO add ID',
        userPoolWebClientId: 'TODO add webclient id',
        identityPoolId: 'TODO add id pool id',
        mandatorySignIn: false,
        oauth: oauth
    }
})

$(function (){
    auth.activate.then((user) =>{
        if (user){
            todo.activate(auth)
        }
    })

})
