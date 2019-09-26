import React, { Component} from "react";
import "./Data.scss";
import DataTable from "./DataTable";
import EditorPane from "../common/EditorPane";

export default
class DataPane extends Component{
  state = {
    currentData: null,
    currentPage: undefined
  };
  
  componentDidMount() {
  }
  
  onShowData = (page) => {
    this.setState({currentData: null, currentPage: page});
  }
  
  onEditData = (data, page) => {
    this.setState({currentData: data, currentPage: page});
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.currentData===null && <DataTable page={this.state.currentPage} onEdit={this.onEditData} onError={this.props.onError}/>}
        {this.state.currentData!==null && <EditorPane record={this.state.currentData} onExit={this.onShowData} onError={this.props.onError}/>}
      </div>
    );
  }
}
