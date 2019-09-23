import React, { Component} from "react";
import "./Settings.scss";
import SettingsForm from "./SettingsForm";

export default
class SettingsPane extends Component {
  
  render(){
    return(
      <div className="SettingsPane">
        <div className="Title">Settings</div>
        <SettingsForm onError={this.props.onError}/>
      </div>
    );
  }
}
