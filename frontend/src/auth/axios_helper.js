import axios from "axios";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import {useEffect, useState} from "react";
import * as AppEndPoints from "../constants/AppEndPoints";
import {UserContext} from "../context/Context";

axios.defaults.baseURL = "http://localhost:8181"
axios.defaults.headers.post["Content-Type"] = "application/json"

let authTokenKey = "auth_token";

export function getAuthToken() {
    return window.localStorage.getItem(authTokenKey);
}

export async function getUser() {
    let u = {};
    await request(
        "GET",
        ApiEndPoints.PROFILE,
        null
    ).then(response => {
        u = response.data;
    }).catch(error => {
        u = null;
        setAuthToken(null);
    })
    return u;
}

export const setAuthToken = (token) => {
    window.localStorage.setItem(authTokenKey, token);
}

export const request = (method, url, data) => {
    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null") {
        headers = {"Authorization": `Bearer ${getAuthToken()}`};
    }
    return axios(
        {
            method: method,
            headers: headers,
            url: url,
            data: data,
        }
    );
}