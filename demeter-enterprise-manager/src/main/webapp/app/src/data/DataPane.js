import React, { Component} from "react";
import "./Data.scss";
import DataTable from "./DataTable";
import SetsList from "./SetsList";
import EditorPane from "../common/EditorPane";

export default
class DataPane extends Component{
  state = {
    currentData: null,
    currentPage: undefined,
    showSets: false
  };
  
  componentDidMount() {
  }
  
  onShowData = (page) => {
    this.setState({currentData: null, currentPage: page, showSets: false});
  }
  
  onEditData = (data, page) => {
    this.setState({currentData: data, currentPage: page, showSets: false});
  }
  
  onShowSets = (data, page) => {
    this.setState({currentData: data, currentPage: page, showSets: true});
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.currentData===null && <DataTable page={this.state.currentPage} onEdit={this.onEditData} onShowSets={this.onShowSets} onError={this.props.onError}/>}
        {this.state.currentData!==null && !this.state.showSets && <EditorPane record={this.state.currentData} onExit={this.onShowData} onError={this.props.onError}/>}
        {this.state.currentData!==null && this.state.showSets && <SetsList record={this.state.currentData} onExit={this.onShowData} onError={this.props.onError}/>}
      </div>
    );
  }
}
