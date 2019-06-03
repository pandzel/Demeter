import React, { Component} from "react";
import "./SetsPane.scss";
import SetsApi from '../api/SetsApi';
import SetsTable from './SetsTable';

export default
class SetsPane extends Component{
  constructor(props) {
    super(props);
    this.state = {};
  }
  
  componentDidMount() {
    const api = new SetsApi();
    api.list().then(result => {
      this.setState({
        data: result
      });
    });
  }
  
  render(){
    return(
      <div className="SetsPane">
        <div className="SetsTitle">Sets</div>
        {this.state.data && <SetsTable data={this.state.data}/>}
      </div>
    );
  }
}
