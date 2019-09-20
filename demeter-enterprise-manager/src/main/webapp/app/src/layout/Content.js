import React, { Component} from "react";
import "./Content.scss";

import SettingsPane from "../settings/SettingsPane";

export default
class Content extends Component {
  
  state = {
    errorMessage: null
  }
  
  onError = (errorMessage) => {
    this.setState({errorMessage});
  }
  
  render(){
    var contentPane;
    switch (this.props.content) {
      case "sets":
        contentPane = "Sets";
        break;
      case "data":
        contentPane = "Data";
        break;
      case "settings":
        contentPane = <SettingsPane onError={this.onError} />;
        break;
    }
    
    return(
      <div className="Content">
        <div className="Error">{this.state.error}</div>
        {contentPane}
      </div>
    );
  }
}
