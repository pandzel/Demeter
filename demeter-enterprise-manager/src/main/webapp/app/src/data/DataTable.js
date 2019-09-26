import React, { Component} from "react";
import "./Data.scss";
import DataApi from '../api/RecordsApi';

export default
class DataTable extends Component{
  state = {
    data: {
      page: 0,
      pageSize: 0,
      total: 0,
      data: []
    }
  };
  
  api = new DataApi();
  
  componentDidMount() {
    this.load();
  }
  
  load = (page) => {
    this.api.list(page).then(data => {
      this.setState({data: data});
    });
  }
  
  render(){
    return(
      <div className="DataTable">
      </div>
    );
  }
}
