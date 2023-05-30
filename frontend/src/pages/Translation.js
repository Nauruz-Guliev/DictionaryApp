import * as React from "react";
import {getUser, request} from "../auth/axios_helper";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import * as LocaleConstants from "../constants/LocaleConstants";
import {DictionaryItem} from "../items/DictionaryItem";
import {ErrorModalWindow} from "../items/ErrorModalWindow";
import * as ErrorMessageConstants from "../constants/ErrorMessageConstants";
import {getAuthToken} from "../auth/axios_helper";

export default class Translation extends React.Component {
    constructor(props) {
        super(props);
        this.timeout = 0;
        this.state = {
            data: [],
            text: "",
            fromLocale: LocaleConstants.RUSSIAN,
            toLocale: LocaleConstants.ENGLISH,
            error: "",
            showModal: false
        };
    }

    onChangeHandler = (event) => {
        let name = event.target.name;
        let value = event.target.value;
        this.onTranslate(value);
        this.setState({[name]: value});
    };

    onTranslate = (word) => {
        try {
            if (this.timeout) clearTimeout(this.timeout);
            if (word.length > 0) {
                this.timeout = setTimeout(() => {
                    console.log(word);
                    request(
                        "POST",
                        ApiEndPoints.TRANSLATION,
                        {
                            text: word,
                            from: this.state.fromLocale,
                            to: this.state.toLocale
                        }
                    ).then((response) => {
                        try {
                            console.log(response.data);
                            this.setState({data: response.data})
                        } catch (e) {
                            this.setState({error: ErrorMessageConstants.ERROR_FETCHING, showModal: true});
                        }
                    }).catch((error) => {
                        try {
                            this.setState({error: error.response.data.message, showModal: true});
                        } catch (e) {
                            this.setState({error: ErrorMessageConstants.ERROR_FETCHING, showModal: true});

                        }
                    });
                }, 600);
            }
        } catch (e) {
            this.setState({error: ErrorMessageConstants.ERROR_FETCHING, showModal: true});
        }
    };

    onLanguageChange = (language) => {
        switch (language.target.outerText) {
            case "Russian":
                this.setState({fromLocale: LocaleConstants.RUSSIAN, toLocale: LocaleConstants.ENGLISH});
                break;
            case "English":
                this.setState({fromLocale: LocaleConstants.ENGLISH, toLocale: LocaleConstants.RUSSIAN});
                break;
        }
    }

    render() {
        return (
            <div className="row justify-content-md-center ms-5 me-5">


                <div className="w-100 mt-5">
                    <div className="row justify-content-start">
                        <div className="btn-group w-25" role="group"
                             aria-label="Basic radio toggle button group">
                            <input type="radio" className="btn-check" value="russian" name="btnradio" id="btnradio1"
                                   autoComplete="off" onChange={() => {
                            }}
                                   checked={this.state.fromLocale === LocaleConstants.RUSSIAN ? true : null}/>
                            <label className="btn btn-outline-dark" htmlFor="btnradio1"
                                   onClick={this.onLanguageChange.bind(this)}>Russian</label>

                            <input type="radio" className="btn-check" value="english" name="btnradio" id="btnradio2"
                                   autoComplete="off" onChange={() => {
                            }}
                                   checked={this.state.fromLocale === LocaleConstants.ENGLISH ? true : null}
                            />
                            <label className="btn btn-outline-dark" htmlFor="btnradio2"
                                   onClick={this.onLanguageChange.bind(this)}>English</label>
                        </div>
                    </div>
                    <form>
                        <div className="row">
                            <div className="form-group mt-2 w-50">
                            <textarea onChange={this.onChangeHandler} className="form-control" name="text" id="text"
                                      rows="3"></textarea>
                            </div>
                            <div className="card w-50">
                                <div className="card-body">
                                    <p className="h5">Translation</p>

                                    <div className="d-flex justify-content-between">
                                        <div className="d-flex flex-row align-items-center">
                                            <p className="small mb-0">{this.state.data.result}</p>
                                        </div>
                                        <div className="d-flex flex-row align-items-xxl-end">
                                            <p className="small text-muted mb-0 align-text-bottom">{!this.state.data.present && this.state.toLocale}</p>
                                        </div>
                                        {this.state.data.present && getUser() &&
                                            <DictionaryItem model={this.state}/>}
                                    </div>
                                </div>
                            </div>

                        </div>

                    </form>


                </div>
                <ErrorModalWindow
                    show={this.state.showModal}
                    onHide={() => this.setState({showModal: false})}
                    message={this.state.error}/>
            </div>
        );
    };
}

function Audio(props) {
    let context = new (window.AudioContext || window.webkitAudioContext)();

    let buf;        // Audio buffer
    let byteArray = props.array;
    if (!byteArray) return;
    let source = context.createBufferSource();
    if (!window.AudioContext) {
        if (!window.webkitAudioContext) {
            alert("Your browser does not support any AudioContext and cannot play back this audio.");
            return;
        }
        window.AudioContext = window.webkitAudioContext;
    }


    let arrayBuffer = new ArrayBuffer(byteArray.length);
    let bufferView = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteArray.length; i++) {
        bufferView[i] = byteArray[i];
    }

    /*

    context.decodeAudioData(
        bufferView,
        (buffer) => {
            source.buffer = buffer;

            source.connect(context.destination);
            source.loop = true;
        },
        (err) => console.error(`Error with decoding audio data: ${err.err}`)
    );

     */
    let blob = new Blob([byteArray], {type: 'audio/ogg'});
    let blobUrl = URL.createObjectURL(blob);


    function play() {
        let audio = document.getElementById("audio");
        audio.play();
    }


    return (
        <audio id="audio" src={blobUrl} controls></audio>
    )
}

