import React, { Component} from "react";
import "./Settings.scss";
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';

export default
class SettingsForm extends Component{
  
  state  = { 
    settings: this.props.settings,
    updated: false
  };
  
  onRepositoryNameChange = (e) => {
    this.setState({settings: {...this.state.settings, repositoryName: e.target.value}, updated: true});
  }
  
  onBaseURLChange = (e) => {
    this.setState({settings: {...this.state.settings, baseURL: e.target.value}, updated: true});
  }
  
  onAdminEmailChange = (e) => {
    this.setState({settings: {...this.state.settings, adminEmail: e.target.value}, updated: true});
  }
  
  onSaveClick = (e) => {
    var data = {...this.state.settings}; //Object.assign(this.state.settings);
    data.adminEmail = data.adminEmail.split(/[ ]*,[ ]*/);
    this.props.onSave(data).then(result => {
      this.setState({updated: false});
    });
  }
  
  render(){
    return(
      <div className="SettingsForm">
        <div>
          <span>Repository name:</span>
          <span>
            <InputText placeholder='Enter repository name' 
                       value={this.state.settings.repositoryName}
                       onChange={this.onRepositoryNameChange}
            />
          </span>
        </div>
        <div>
          <span>Base URL:</span>
          <span>
            <InputText placeholder='Enter repository base URL' 
                       value={this.state.settings.baseURL}
                       onChange={this.onBaseURLChange}
            />
          </span>
        </div>
        <div>
          <span>Administrators emails:</span>
          <span>
            <InputText placeholder='List administrators email(s)' 
                       value={this.state.settings.adminEmail}
                       onChange={this.onAdminEmailChange}
            />
          </span>
        </div>
        <Button label='Save' disabled={!this.state.updated} onClick={this.onSaveClick}/>
      </div>
    );
  }
}
