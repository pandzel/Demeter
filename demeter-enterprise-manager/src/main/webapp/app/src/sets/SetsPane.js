import React, { Component} from "react";
import "./Sets.scss";
import SetsTable from "./SetsTable";
import SetRecords from "./SetRecords";

export default
class SetsPane extends Component{
  state = {
    currentSet: null
  };
  
  componentDidMount() {
  }
  
  onShowRecords = (props) => {
    this.setState({currentSet: props});
  }
  
  onShowSets = (props) => {
    this.setState({currentSet: null});
  }
  
  render(){
    return(
      <div className="SetsPane">
        <div className="Title">Sets</div>
        {this.state.currentSet===null && <SetsTable onShowRecords={this.onShowRecords} onError={this.props.onError}/>}
        {this.state.currentSet!==null && <SetRecords onExit={this.onShowSets} onError={this.props.onError}/>}
      </div>
    );
  }
}
