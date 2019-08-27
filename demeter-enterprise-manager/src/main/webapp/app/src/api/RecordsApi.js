import axios from 'axios';

export default
class RecordsApi {
  list() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/records`).then((result)=>resolve(result.data.data));
    });
  }
}