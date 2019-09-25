import React, { Component} from "react";
import "./Sets.scss";
import SetsTable from "./SetsTable";

export default
class SetsPane extends Component{
  state = {};
  
  componentDidMount() {
  }
  
  onShowRecords = (props) => {
    console.log("Showing records for", props);
  }
  
  render(){
    return(
      <div className="SetsPane">
        <div className="Title">Sets</div>
        <SetsTable onShowRecords={this.onShowRecords} onError={this.props.onError}/>
      </div>
    );
  }
}
