import React, { Component} from "react";
import "./SettingsPane.scss";
import ConfigApi from '../api/ConfigApi';
import SettingsForm from './SettingsForm';

export default
class SettingsPane extends Component{
  state = {};
  
  api = new ConfigApi();
  
  componentDidMount() {
    this.api.load().then(result => {
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
        {this.state.data && <SettingsForm data={this.state.data}/>}
      </div>
    );
  }
}
