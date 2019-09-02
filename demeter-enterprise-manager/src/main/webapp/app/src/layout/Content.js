import React, { Component} from "react";
import "./Content.scss";

import HomePane     from '../home/HomePane';
import SetsPane     from '../sets/SetsPane';
import DataPane     from '../data/DataPane';
import ToolsPane    from '../tools/ToolsPane';
import SettingsPane from '../settings/SettingsPane';

export default
class Content extends Component{
  
  render(){
    var contentPane;
    switch (this.props.content) {
      case "home":
        contentPane = <HomePane/>;
        break;
      case "sets":
        contentPane = <SetsPane/>;
        break;
      case "data":
        contentPane = <DataPane/>;
        break;
      case "tools":
        contentPane = <ToolsPane/>;
        break;
      case "settings":
        contentPane = <SettingsPane/>;
        break;
      default:
        contentPane = <DataPane/>;
        break;
    }
    return(
      <div className="Content">
        {contentPane}
      </div>
    );
  }
}
