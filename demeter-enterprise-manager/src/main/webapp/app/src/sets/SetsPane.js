import React, { Component} from "react";
import "./Sets.scss";
import SetsTable from "./SetsTable";

export default
class SetsPane extends Component{
  state = {};
  
  componentDidMount() {
  }
  
  render(){
    return(
      <div className="SetsPane">
        <div className="Title">Sets</div>
        <SetsTable onError={this.props.onError}/>
      </div>
    );
  }
}
