'use strict'

import $ from 'jquery'
import {view} from './view'
import {Auth} from 'aws-amplify'

const auth = {activate,user,session}
export {auth,user}

function activate(){
    return new Promise((resolve,reject)=>{
        Auth.currentAuthenticatedUser()
            .then(user =>{
                view.renderLink(true)
                bindLinks()
                resolve(user)
            })
            .catch(()=>{
                view.renderLink(false)
                bindLinks()
                resolve(null)
            })
    })
}


function bindLinks(){
    $('#logout').unbind('click')
    $('#logout').on('click',e=>{
        Auth.signOut().catch(()=>{})
    })

    $('#login').unbind('click')
    $('#login').on('click',e=>{
        const config = Auth.configure()
        const {domain,redirectSignIn,responseType} = config.oauth
        const clientId = config.userPoolWebClientId
        const url = 'https://' + domain + '/login?response_type=' + responseType + '&client_id=' + clientId
        + '&redirect_uri=' + redirectSignIn

        window.location.assign(url)
    })
}

function user(){
    return Auth.currentAuthenticatedUser()
}

function session(){
    return Auth.currentSession()
}
