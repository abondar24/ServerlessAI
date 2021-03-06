'use strict'

export {addTpl, editTpl, errTpl, todoListTpl, navBarTpl, navbarScheduleTpl}


function todoListTpl(items) {
    let output = ''
    items.forEach(item => {
        output += todoItemTpl(item)
    })

    return `
    <div id="todo-list">
      <div class="row list-group-item d-flex ">
          <div class="col-sm-1"></div>
          <div class="col-sm-2">Due</div>
          <div class="col-sm-3">Action</div
          <div class="col-sm-3">Notes</div>
          <div class="col-sm-1"></div>
          <div class="col-sm-1"></div>
      </div>
      ${output}
    </div>
    <div id="edit-area" class="list-group">
       <li class="list-group-item d-flex justify-content-between align-items-center">
          <span id="input-todo" class="badge bg-success rounded-pill">New</span>
       </li>
    </div> `
}

function todoItemTpl(item) {
    return `
    <div id="${item.id}" class="row list-group-item d-flex justify-content-between align-items-center">
        <div class="col-sm-1"></div>
        <div id="due-date" class="col-sm-2">${item.dueDate}</div>
        <div id="action" class="col-sm-3">${item.action}</div>
        <div id="note" class="col-sm-3">${item.note === null ? '' : item.note}</div>
        <div id="stat" class="col-sm-1 badge bg-secondary rounded-pill">${item.stat ? 'Done': 'Active'}</div>
        <div id="${item.id}" class="col-sm-1 btn btn-danger rounded-pill todo-item-delete">Delete</div>
        <div id="${item.id}" class="col-sm-1 btn btn-warning rounded-pill todo-item-edit">Edit</div>
    </div>
    `
}

function editTpl() {
    return `
       <div class="row">&nbsp;</div>
    <div class="row">
      <div class="col-sm-6">
        <div class="row">
          <div class="col-sm-1"></div><div class="col-sm-1">Due: </div><div class="col-sm-6"><input  class="w-100" type="text" id="todo-duedate"></div>
        </div>
        <div class="row">&nbsp;</div>
        <div class="row">
          <div class="col-sm-1"></div><div class="col-sm-1">Action: </div><div class="col-sm-6"><input class="w-100" type="text" id="todo-action"></div>
        </div>
        <div class="row">&nbsp;</div>
        <div class="row">
          <div class="col-sm-1"></div><div class="col-sm-1">Done: </div><div class="col-sm-6"><input type="checkbox" id="todo-stat"></div>
        </div>
        <div class="row">&nbsp;</div>
        <div class="row">
          <div class="col-sm-1"></div>
          <div class="col-sm-1"><button id="todo-save" type="button" class="btn-primary">Save</button></div>
          <div class="col-sm-1"><button id="todo-cancel" type="button" class="btn-secondary">Cancel</button></div>
          <input type="hidden" id="todo-id">
        </div>
      </div>
      <div class="col-sm-6">
        <div class="row">
          <div class="col-sm-1">Note: </div>
          <div class="col-sm-6">
            <textarea id="todo-note" rows="5" cols="50" maxlength="5000" wrap="hard"></textarea>
          </div>
        </div>
        <div class="row"&nbsp;</div>
        <div class="row">
           <div class="col-sm-1"></div>
           <div class="col-sm-2"><button id="todo-note-start">record</button></button></div>
           <div class="col-sm-1"></div>
           <div class="col-sm-2"><button id="todo-note-stop">stop</button></div>
        </div>
      </div>
    </div>`
}

function addTpl(){
    return `<li class="list-group-item d-flex justify-content-between align-items-center">
          <span id="input-todo" class="badge bg-success rounded-pill">New</span>
       </li>`
}

function errTpl(err){
    return `<div class="error">${JSON.stringify(err)}</div>`
}

function navBarTpl(isAuth){
    let link

    if (isAuth){
        link = '<a class="nav-link" href="#" id="logout">Logout</a>'
    } else {
        link = '<a class="nav-link" href="#" id="login">Login</a>'
    }

    return `
    <ul class="navbar-nav" id="navbar-list">
      <li class="nav-item">
        ${link}
      </li>
    </ul>`
}

function navbarScheduleTpl(){
    return `
      <li class="nav-item">
         <button id="todo-schedule">schedule</button>
     </li>`
}
