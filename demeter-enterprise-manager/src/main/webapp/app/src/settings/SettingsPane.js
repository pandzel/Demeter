import React, { Component} from "react";
import "./Settings.scss";
import ConfigApi from '../api/ConfigApi';

export default
class SettingsPane extends Component{
  state = {
    settings: null
  };
  
  api = new ConfigApi();
  
  componentDidMount() {
    this.api.load().then(result => {
      this.props.onError(null);
      result.adminEmail = result.adminEmail? result.adminEmail.join(', '): '';
      console.log(result);
      this.setState({settings: result});
    }).catch(this.props.onError);
  }
  
  render(){
    return(
      <div className="SettingsPane">
        <div className="Title">Settings</div>
        {this.state.settings && this.state.settings.repositoryName}
      </div>
    );
  }
}
