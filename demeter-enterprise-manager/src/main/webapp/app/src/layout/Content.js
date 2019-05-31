import React, { Component} from "react";
import "./Content.scss";

import Home from '../home/Home';
import Sets from '../sets/Sets';
import Data from '../data/Data';
import Tools from '../tools/Tools';
import Settings from '../settings/Settings';

export default
class Content extends Component{
  constructor(props) {
    super(props);
  }
  
  render(){
    var contentPane = <Home/>;
    switch (this.props.content) {
      case "home":
        contentPane = <Home/>;
        break;
      case "sets":
        contentPane = <Sets/>;
        break;
      case "data":
        contentPane = <Data/>;
        break;
      case "tools":
        contentPane = <Tools/>;
        break;
      case "settings":
        contentPane = <Settings/>;
        break;
      default:
        contentPane = <Home/>;
        break;
    }
    return(
      <div className="Content">
        {contentPane}
      </div>
    );
  }
}
