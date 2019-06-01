import React, { Component} from "react";
import "./Navi.scss";
import iconHome from '../images/icon-home.png';
import iconSets from '../images/icon-sets.png';
import iconFiles from '../images/icon-files.png';
import iconTools from '../images/icon-tools.png';
import iconSettings from '../images/icon-settings.png';

export default
class Navi extends Component{
  
  constructor(props) {
    super(props);
    this.state = { selected: 'home' };
  };
  
  handleClick(e) {
    this.setState({ selected: e });
    this.props.onNaviClick({content: e});
  };
  
  render(){
    return(
      <div className="Navi">
        <img src={iconHome}     width="80" height="80" title="Home"
            onClick={()=>this.handleClick('home')}
            className={this.state.selected=='home'? 'selected': ''}/>
            
        <img src={iconFiles}    width="80" height="80" title="Data"
            onClick={()=>this.handleClick('data')}
            className={this.state.selected=='data'? 'selected': ''}/>
            
        <img src={iconSets}     width="80" height="80" title="Sets"
            onClick={()=>this.handleClick('sets')}
            className={this.state.selected=='sets'? 'selected': ''}/>
            
        <img src={iconTools}    width="80" height="80" title="Tools"
            onClick={()=>this.handleClick('tools')}
            className={this.state.selected=='tools'? 'selected': ''}/>
            
        <img src={iconSettings} width="80" height="80" title="Settings" 
            onClick={()=>this.handleClick('settings')}
            className={this.state.selected=='settings'? 'selected': ''}/>
      </div>
    );
  };
}
