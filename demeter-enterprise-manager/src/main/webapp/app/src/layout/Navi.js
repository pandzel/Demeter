import React, { Component} from "react";
import "./Navi.scss";
import iconHome from '../images/icon-home.png';
import iconSets from '../images/icon-sets.png';
import iconFiles from '../images/icon-files.png';
import iconTools from '../images/icon-tools.png';
import iconSettings from '../images/icon-settings.png';

export default
class Navi extends Component{
  
  state = {
    selected: 'home'
  }
  
  handleClick = (e) => {
    const selected = e.target.attributes.value.value;
    this.setState({ selected });
    this.props.onNaviClick({content: selected});
  };
  
  render(){
    return(
      <div className="Navi">
        <img src={iconHome}     width="80" height="80" title="Home" value="home"
            onClick={this.handleClick}
            className={this.state.selected=='home'? 'selected': ''}/>
            
        <img src={iconFiles}    width="80" height="80" title="Data" value="data"
            onClick={this.handleClick}
            className={this.state.selected=='data'? 'selected': ''}/>
            
        <img src={iconSets}     width="80" height="80" title="Sets" value="sets"
            onClick={this.handleClick}
            className={this.state.selected=='sets'? 'selected': ''}/>
            
        <img src={iconTools}    width="80" height="80" title="Tools" value="tools"
            onClick={this.handleClick}
            className={this.state.selected=='tools'? 'selected': ''}/>
            
        <img src={iconSettings} width="80" height="80" title="Settings" value="settings"
            onClick={this.handleClick}
            className={this.state.selected=='settings'? 'selected': ''}/>
      </div>
    );
  };
}
