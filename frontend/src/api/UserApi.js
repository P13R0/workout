import axios from 'axios'

export default {
  login: (username, password) => axios.post('/api/login', {}, { auth: { username, password } })
}
