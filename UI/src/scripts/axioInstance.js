import axios from "axios";

export const AxiosInstance = axios.create({
  baseURL: "http://localhost",
});

AxiosInstance.interceptors.response.use(
  (response) => {
    // console.log("Api call done");
    return response;
  },
  (error) => {
    // console.error("Error:", error);
    // console.log("Api call done with error");
    return Promise.reject(error);
  }
);

export default AxiosInstance;

// const url = "http://localhost:5100/access/generate-otp/" + email;
// AxiosInstance.post(url, "")
//   .then((response) => {
//     console.log(response);
//   })
//   .catch((error) => {
//     setWarning(error.response.data.message);
//   });
// setIsOtpSent(true);
// const countdown = setInterval(() => {
//   setTimer((prevTimer) => prevTimer - 1);
// }, 1000);
// setTimeout(() => {
//   clearInterval(countdown);
//   setIsOtpSent(false);
//   setTimer(30);
// }, 30000);
