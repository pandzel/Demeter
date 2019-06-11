import React, { Component} from "react";
import "./SettingsPane.scss";
import ConfigApi from '../api/ConfigApi';
import {InputText} from 'primereact/inputtext';

export default
class SettingsPane extends Component{
  state = {};
  
  componentDidMount() {
    const api = new ConfigApi();
    api.load().then(result => {
      result.adminEmail = result.adminEmail? result.adminEmail.join(', '): '';
      this.setState({
        data: result
      });
    });
  }
  
  render(){
    return(
      <div className="SettingsPane">
        <div className="Title">Settings</div>
        <div className="SettingsForm">
          <div>
            <span>Repository name:</span><span><InputText value={this.state.repositoryName} placeholder='Enter repository name'/></span>
          </div>
          <div>
            <span>Base URL:</span><span><InputText value={this.state.baseURL} placeholder='Enter repository base URL'/></span>
          </div>
          <div>
            <span>Administrators emails:</span><span><InputText value={this.state.adminEmail} placeholder='List administrators email(s)'/></span>
          </div>
        </div>
      </div>
    );
  }
}
