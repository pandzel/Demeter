import React, { Component} from "react";
import "./Content.scss";

export default
class Content extends Component{
  constructor(props) {
    super(props);
  }
  
  render(){
    if (this.props.content==="home") {
      return(
        <div className="Content">
          {this.props.content}
        </div>
      );
    } else {
      return(
        <div className="Content">
          {this.props.content}
        </div>
      );
    }
  }
}
