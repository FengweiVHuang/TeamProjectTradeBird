import axios from 'axios';

const axiosApi = axios.create({
    baseURL: '/api',
    withCredentials: true
});

export default axiosApi;