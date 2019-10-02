import React, { Component} from "react";
import "./Sets.scss";
import SetsTable from "./SetsTable";
import SetRecords from "./SetRecords";

export default
class SetsPane extends Component{
  state = {
    title: "",
    currentSet: null
  };
  
  componentDidMount() {
  }
  
  onShowRecords = (props) => {
    this.setState({currentSet: props, title: props.setName? `(${props.setName})`: ""});
  }
  
  onShowSets = (props) => {
    this.setState({currentSet: null, title: ""});
  }
  
  render(){
    return(
      <div className="SetsPane">
        <div className="Title">Sets <span className="TitleSuffix">{this.state.title}</span></div>
        {this.state.currentSet===null && <SetsTable onShowRecords={this.onShowRecords} onError={this.props.onError}/>}
        {this.state.currentSet!==null && <SetRecords cols={2} currentSet={this.state.currentSet} onExit={this.onShowSets} onError={this.props.onError}/>}
      </div>
    );
  }
}
