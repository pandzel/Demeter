import React, { Component} from "react";
import "./Settings.scss";
import ConfigApi from '../api/ConfigApi';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';

export default
class SettingsForm extends Component {
  
  state  = { 
    settings: {
      repositoryName: "",
      baseURL: "",
      adminEmail: ""
    },
    updated: false
  };
  
  api = new ConfigApi();
  
  
  componentDidMount() {
    this.api.load().then(settings => {
      this.props.onError(null);
      settings.adminEmail = settings.adminEmail? settings.adminEmail.join(', '): '';
      this.setState({settings});
    }).catch(this.props.onError);
  }
  
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
    var settings = {...this.state.settings};
    settings.adminEmail = settings.adminEmail.split(/[ ]*,[ ]*/);
    this.api.save(settings).then(result => {
      this.props.onError(null);
      this.setState({updated: false});
    }).catch(this.props.onError);
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
