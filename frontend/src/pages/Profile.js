import * as React from 'react';
import LoginForm from "../items/LoginForm";
import {getUser, request, setAuthToken} from "../auth/axios_helper";
import * as ComponentNames from "../constants/ComponentNames";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import * as AppEndPoints from "../constants/AppEndPoints";
import {Navigate, useMatch, useNavigate, useResolvedPath} from "react-router-dom";
import {ErrorModalWindow} from "../items/ErrorModalWindow";
import {Form} from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import {UserContext} from "../context/Context";

import * as ErrorMessageConstants from "../constants/ErrorMessageConstants";


export default function Profile(props) {


    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [password, setPassword] = useState("");
    const [passwordRepeat, setPasswordRepeat] = useState("");
    const [error, setError] = useState("");
    const [showModal, setShowModal] = useState(false);

    const [user, setUser] = useContext(UserContext);

    let navigate = useNavigate();


    function onSubmitDelete(e) {
        e.preventDefault();
        try {
            request("DELETE", ApiEndPoints.PROFILE)
                .then((response) => {
                    setUser(null);
                    navigate(AppEndPoints.HOME);
                    setAuthToken(null);
                }).catch((error) => {
                setError(error.result.data);
            });
        } catch (e) {
            setError(ErrorMessageConstants.ERROR_FETCHING);
            setShowModal(true);
        }
    }

    function onSubmitUpdate(e) {
        e.preventDefault();
        try {
            request("PUT", ApiEndPoints.PROFILE, {
                id: user.id,
                login: user.login,
                firstName: firstName,
                lastName: lastName,
                password: password,
                passwordRepeat: passwordRepeat
            })
                .then((response) => {
                    navigate(AppEndPoints.HOME);
                }).catch((error) => {
                console.error(error)
                setError(error.response.data.message);
                setShowModal(true);
            });
        } catch (e) {
            setError(ErrorMessageConstants.ERROR_FETCHING);
            setShowModal(true);
        }
    }


    function onChangeHandler(event) {
        let name = event.target.name;
        let value = event.target.value;
        console.log(value)
        if (name === "password") {
            setPassword(value);
        }
        if (name === "passwordRepeat") {
            setPasswordRepeat(value);
        }
        if (name === "firstName") {
            setFirstName(value);
        }
        if (name === "lastName") {
            setLastName(value);
        }
    }

    try {
        useEffect(() => {
            request(
                "GET",
                ApiEndPoints.PROFILE,
                null
            ).then((response) => {
                setFirstName(response.data.firstName);
                setLastName(response.data.lastName);
            }).catch((error) => {
                setError(error.response.data.message);
                setShowModal(true);
                navigate(AppEndPoints.LOGIN)
            });
        }, []);
    } catch (e) {
        setError(ErrorMessageConstants.ERROR_FETCHING);
        setShowModal(true);
    }
    //            {state.success && <Navigate to={AppEndPoints.HOME} replace={false}/>}

    return (
        <div className="mt-5">


            <div className="container">
                <div className="row gutters">
                    <div className="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">
                        <div className="card h-100">
                            <div className="card-body">
                                <div className="row gutters">
                                    <div className="w-100">
                                        <h6 className="mb-2 text-primary">Personal Details</h6>
                                    </div>
                                    <div className="w-100">
                                        <div className="form-group">
                                            <label htmlFor="firstName" className="pt-2">First name</label>
                                            <input type="text" name="firstName" className="form-control"
                                                   id="firstName" onChange={onChangeHandler}
                                                   value={firstName}/>
                                        </div>

                                        <div className="form-group">
                                            <label htmlFor="lastName" className="pt-2">Last name</label>
                                            <input type="text" name="lastName" onChange={onChangeHandler}
                                                   className="form-control" id="lastName"
                                                   value={lastName}
                                            />
                                        </div>

                                        <div className="form-group">
                                            <label htmlFor="password" className="pt-2">Password</label>
                                            <input type="password" onChange={onChangeHandler} name="password"
                                                   className="form-control"
                                                   id="password"/>
                                        </div>

                                        <div className="form-group">
                                            <label htmlFor="password" className="pt-2">Password repeat</label>
                                            <input type="password" onChange={onChangeHandler}
                                                   name="passwordRepeat" className="form-control"
                                                   id="passwordRepeat"/>
                                        </div>
                                    </div>

                                </div>
                                <div className="d-flex flex-row mt-5 w-100">
                                    <button type="button" id="submit" name="submit"
                                            className="btn btn-primary w-50 me-3"
                                            onClick={onSubmitUpdate}>Update
                                    </button>

                                    <button type="button" id="submit" name="submit"
                                            className="btn btn-danger w-50 ms-3"
                                            onClick={onSubmitDelete}>Delete
                                    </button>
                                </div>
                                <ErrorModalWindow
                                    show={showModal}
                                    onHide={() => setShowModal(false)}
                                    message={error}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>)
//    {state.navigateHome && <Navigate to={AppEndPoints.HOME} replace={true}/>}

}
/*
export default class Profile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            firstName: "",
            lastName: "",
            password: "",
            passwordRepeat: "",
            success: false,
            error: "",
            showModal: false,
            navigateHome: false
        }
    }

    onChangeHandler = (event) => {
        let name = event.target.name;
        let value = event.target.value;
        this.setState({[name]: value});
    };


    componentDidMount() {
        request(
            "GET",
            ApiEndPoints.PROFILE,
            null
        ).then((response) => {
            this.setState({
                firstName: response.data.firstName,
                lastName: response.data.lastName,
            })
        }).catch((error) => {
            this.setState({error: error.response.data.message, showModal: true});
        });
    };

    onSubmitUpdate = (e) => {
        e.preventDefault();
        request("PUT", ApiEndPoints.PROFILE, {
            id: "",
            login: "",
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            password: this.state.password,
            passwordRepeat: this.state.passwordRepeat
        })
            .then((response) => {
                this.setState({navigateHome: true})
            }).catch((error) => {
            this.setState({error: error.response.data.message, showModal: true});
        });
    };

    onSubmitDelete = (e) => {
        e.preventDefault();
        request("DELETE", ApiEndPoints.PROFILE,
        )
            .then((response) => {
                this.setState({navigateHome: true})
                setAuthToken(null);
            }).catch((error) => {
            this.setState({error: error.response.data.message, showModal: true});
        });
    };

    render() {
        return (
            <div className="mt-5">

                {this.state.success && <Navigate to={AppEndPoints.HOME} replace={false}/>}

                <div className="container">
                    <div className="row gutters">
                        <div className="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">
                            <div className="card h-100">
                                <div className="card-body">
                                    <div className="row gutters">
                                        <div className="w-100">
                                            <h6 className="mb-2 text-primary">Personal Details</h6>
                                        </div>
                                        <div className="w-100">
                                            <div className="form-group">
                                                <label htmlFor="firstName" className="pt-2">First name</label>
                                                <input type="text" name="firstName" className="form-control"
                                                       id="firstName" onChange={this.onChangeHandler}
                                                       value={this.state.firstName}/>
                                            </div>

                                            <div className="form-group">
                                                <label htmlFor="lastName" className="pt-2">Last name</label>
                                                <input type="text" name="lastName" onChange={this.onChangeHandler}
                                                       className="form-control" id="lastName"
                                                       value={this.state.lastName}
                                                />
                                            </div>

                                            <div className="form-group">
                                                <label htmlFor="password" className="pt-2">Password</label>
                                                <input type="password" onChange={this.onChangeHandler} name="password"
                                                       className="form-control"
                                                       id="password"/>
                                            </div>

                                            <div className="form-group">
                                                <label htmlFor="password" className="pt-2">Password repeat</label>
                                                <input type="password" onChange={this.onChangeHandler}
                                                       name="passwordRepeat" className="form-control"
                                                       id="passwordRepeat"/>
                                            </div>
                                        </div>

                                    </div>
                                    <div className="d-flex flex-row mt-5 w-100">
                                        <button type="button" id="submit" name="submit"
                                                className="btn btn-primary w-50 me-3"
                                                onClick={this.onSubmitUpdate}>Update
                                        </button>

                                        <button type="button" id="submit" name="submit"
                                                className="btn btn-danger w-50 ms-3"
                                                onClick={this.onSubmitDelete}>Delete
                                        </button>
                                    </div>
                                    <ErrorModalWindow
                                        show={this.state.showModal}
                                        onHide={() => this.setState({showModal: false})}
                                        message={this.state.error}
                                    />
                                    {this.state.navigateHome && <Navigate to={AppEndPoints.HOME} replace={true}/>}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>)
    }
}

 */