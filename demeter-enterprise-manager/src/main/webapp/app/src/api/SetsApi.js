import axios from 'axios';

export default
class SetsApi {
  list() {
    return new Promise((resolve, reject) => {
      axios.get('rest/sets').then((result)=>resolve(result.data));
    });
  }
}