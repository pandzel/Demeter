import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import RecordsTable from './RecordsTable';

export default
class DataPane extends Component{
  state = {};
  
  componentDidMount() {
    const api = new RecordsApi();
    api.list().then(result => {
      this.setState({
        data: result
      });
    });
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.data && <RecordsTable data={this.state.data}/>}
      </div>
    );
  }
}
