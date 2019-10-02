import React, { Component} from "react";
import "./Data.scss";
import DataTable from "./DataTable";
import RecordSets from "./RecordSets";
import EditorPane from "../common/EditorPane";

export default
class DataPane extends Component{
  state = {
    title: "",
    currentData: null,
    currentPage: undefined,
    showSets: false
  };
  
  componentDidMount() {
  }
  
  onShowData = (page) => {
    this.setState({currentData: null, currentPage: page, showSets: false, title: ""});
  }
  
  onEditData = (data, page) => {
    this.setState({currentData: data, currentPage: page, showSets: false, title: data.title? `(${data.title})`: ""});
  }
  
  onShowSets = (data, page) => {
    this.setState({currentData: data, currentPage: page, showSets: true, title: data.title? `(${data.title})`: ""});
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data <span className="TitleSuffix">{this.state.title}</span></div>
        {this.state.currentData===null && <DataTable page={this.state.currentPage} onEdit={this.onEditData} onShowSets={this.onShowSets} onError={this.props.onError}/>}
        {this.state.currentData!==null && !this.state.showSets && <EditorPane record={this.state.currentData} onExit={this.onShowData} onError={this.props.onError}/>}
        {this.state.currentData!==null && this.state.showSets && <RecordSets record={this.state.currentData} onExit={this.onShowData} onError={this.props.onError}/>}
      </div>
    );
  }
}
