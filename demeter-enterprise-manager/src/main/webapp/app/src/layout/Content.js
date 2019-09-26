import React, { Component} from "react";
import "./Content.scss";

import SettingsPane from "../settings/SettingsPane";
import SetsPane from "../sets/SetsPane";
import DataPane from "../data/DataPane";

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
      case "data":
        contentPane = <DataPane onError={this.onError} />;
        break;
      case "sets":
        contentPane = <SetsPane onError={this.onError} />;
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
