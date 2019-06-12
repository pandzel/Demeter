import React, { Component} from "react";
import "./SettingsPane.scss";
import ConfigApi from '../api/ConfigApi';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';

export default
class SettingsForm extends Component{
  
  state  = { 
    data: this.props.data,
    updated: false
  };
  
  api = new ConfigApi();
  
  onRepositoryNameChange = (e) => {
    this.state.data.repositoryName = e.target.value;
    this.state.updated = true;
    this.setState(this.state);
  }
  
  onBaseURLChange = (e) => {
    this.state.data.baseURL = e.target.value;
    this.state.updated = true;
    this.setState(this.state);
  }
  
  onAdminEmailChange = (e) => {
    this.state.data.adminEmail = e.target.value;
    this.state.updated = true;
    this.setState(this.state);
  }
  
  onSaveClick = (e) => {
    this.api.save(this.state.data).then(result => {
      this.state.updated = false;
      this.setState(this.state);
    });
  }
  
  render(){
    return(
      <div className="SettingsForm">
        <div>
          <span>Repository name:</span>
          <span>
            <InputText placeholder='Enter repository name' 
                       value={this.state.data.repositoryName}
                       onChange={this.onRepositoryNameChange}
            />
          </span>
        </div>
        <div>
          <span>Base URL:</span>
          <span>
            <InputText placeholder='Enter repository base URL' 
                       value={this.state.data.baseURL}
                       onChange={this.onBaseURLChange}
            />
          </span>
        </div>
        <div>
          <span>Administrators emails:</span>
          <span>
            <InputText placeholder='List administrators email(s)' 
                       value={this.state.data.adminEmail}
                       onChange={this.onAdminEmailChange}
            />
          </span>
        </div>
        <Button label='Save' disabled={!this.state.updated} onClick={this.onSaveClick}/>
      </div>
    );
  }
}
