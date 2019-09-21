import React, { Component} from "react";
import "./Settings.scss";
import ConfigApi from '../api/ConfigApi';
import SettingsForm from "./SettingsForm";

export default
class SettingsPane extends Component{
  state = {};
  
  api = new ConfigApi();
  
  componentDidMount() {
    this.api.load().then(result => {
      this.props.onError(null);
      result.adminEmail = result.adminEmail? result.adminEmail.join(', '): '';
      this.setState({settings: result});
    }).catch(this.props.onError);
  }
  
  onSave = (data) => {
    return new Promise((resolve, reject) => {
      this.api.save(data).then(result => {
        this.props.onError(null);
        resolve(result);
      }).catch(error => {
        this.props.onError(error);
        reject(error);
      });
    });
  }
  
  render(){
    return(
      <div className="SettingsPane">
        <div className="Title">Settings</div>
        {this.state.settings && <SettingsForm settings={this.state.settings} onSave={this.onSave}/>}
      </div>
    );
  }
}
