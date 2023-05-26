import * as React from 'react';
import LoginForm from "../items/LoginForm";
import {getUser, request, setAuthToken, setUserId, setUserLogin} from "../auth/axios_helper";
import * as ComponentNames from "../constants/ComponentNames";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import * as AppEndPoints from "../constants/AppEndPoints";
import {Navigate, useLocation, useMatch, useNavigate, useResolvedPath} from "react-router-dom";
import {ErrorModalWindow} from "../items/ErrorModalWindow";
import {useContext, useEffect, useState} from "react";
import {UserContext} from "../context/Context";
import * as ErrorMessageConstants from "../constants/ErrorMessageConstants";


export default function Authentication(props) {
    const [user, setUser] = useContext(UserContext);

    const [componentToShow] = useState(ComponentNames.LOGIN);
    const [error, setError] = useState("");
    const [showModal, setShowModal] = useState(false);

    const navigate = useNavigate()


    function onRegister(e, firstName, lastName, userName, password) {
        e.preventDefault();

        request("POST", ApiEndPoints.REGISTER, {
            firstName: firstName, lastName: lastName, login: userName, password: password
        }).then((response) => {
            response && response.data && setAuthToken(response.data.token);
            setUser(response.data);
            navigate(AppEndPoints.HOME);
        }).catch((error) => {
            try {
                setError(error.response.data.message);
                setShowModal(true)
                setAuthToken(null);
            } catch (e) {
                setError(ErrorMessageConstants.ERROR_FETCHING);
                setShowModal(true);
            }
        });
    }

    function onLogin(e, username, password) {
        e.preventDefault();
        request("POST", ApiEndPoints.LOGIN, {login: username, password: password})
            .then((response) => {
                console.log(response);
                response && response.data && setAuthToken(response.data.token);
                setUser(response.data);
                navigate(AppEndPoints.HOME);
            }).catch((error) => {
            try {
                console.error(error);
                setError(error.response.data.message);
                setShowModal(true)
                setAuthToken(null);
            } catch (e) {
                setError(ErrorMessageConstants.ERROR_FETCHING);
                setShowModal(true);
            }
        });
    }

    return (<div>
            <LoginForm onLogin={onLogin} onRegister={onRegister}/>
            <ErrorModalWindow
                show={showModal}
                onHide={() => setShowModal(false)}
                message={error}
            />
        </div>
    );

}
