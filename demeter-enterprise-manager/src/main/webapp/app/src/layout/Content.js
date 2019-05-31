import React, { Component} from "react";
import "./Content.scss";

export default
class Content extends Component{
  constructor(props) {
    super(props);
  }
  
  render(){
    return(
      <div className="Content">
        {this.props.content}
      </div>
    );
  }
}
