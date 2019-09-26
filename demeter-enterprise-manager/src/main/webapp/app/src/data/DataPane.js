import React, { Component} from "react";
import "./Data.scss";
import DataTable from "./DataTable";
import EditorPane from "../common/EditorPane";

export default
class DataPane extends Component{
  state = {
    currentData: null
  };
  
  componentDidMount() {
  }
  
  onShowData = (page) => {
    this.setState({currentData: null});
  }
  
  onEditData = (data) => {
    this.setState({currentData: data});
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.currentData===null && <DataTable onEdit={this.onEdit} onError={this.props.onError}/>}
        {this.state.currentData!==null && <EditorPane onExit={this.onShowData} onError={this.props.onError}/>}
      </div>
    );
  }
}
