import * as React from "react";
import * as EndPoints from "../constants/AppEndPoints";
import {request} from "../auth/axios_helper";
import * as ApiEndPoints from "../constants/ApiEndPoints";

export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }
    componentDidMount() {
        request("GET", "/test", {
        })
            .then((response) => {
                try {
                    this.setState({data: response.data})
                    console.log(this.state.data.dictionary)
                } catch (e) {

                }
            }).catch((error) => {
            console.log(error)
        })
    }
    render() {
        return (
            <div className="cover-container d-flex h-100 p-3 mx-auto flex-column">
                <header className="masthead mb-auto">
                    <div className="inner">
                        <h3 className="masthead-brand"> { this.state.data && this.state.data.length > 0 && <p>{this.state.data[0]}</p> } </h3>
                    </div>
                </header>

                <main role="main" className="inner cover">
                    <h1 className="cover-heading">Cover your page.</h1>
                    <p className="lead mt-5">
                        <a href={EndPoints.TRANSLATION} className="btn btn-lg btn-secondary">Learn more words</a>
                    </p>
                </main>
            </div>


        );
    }
}