package com.cvte.taobaounion.presenter;

import com.cvte.taobaounion.model.Api;
import com.cvte.taobaounion.model.TicketPramas;
import com.cvte.taobaounion.model.domain.TicketResult;
import com.cvte.taobaounion.utils.RetrofitManager;
import com.cvte.taobaounion.utils.UrlUtils;
import com.cvte.taobaounion.view.ITicketPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {
    private ITicketPageCallback mViewCallback = null;
    private String mCover = null;
    public TicketResult ticketResult;

    enum State {
        LOADING, SUCCESS, ERROR, NONE;
    }

    private State currentState = State.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        currentState = State.LOADING;
        this.onTicketLoad();
        this.mCover = cover;
        String ticketUrl = UrlUtils.getTicketUrl(url);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketPramas ticketPramas = new TicketPramas(ticketUrl, title);
        Call<TicketResult> ticket = api.getTicket(ticketPramas);
        ticket.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    ticketResult = response.body();
                    currentState = State.SUCCESS;
                    if (mViewCallback != null) {
                        mViewCallback.onTicketLoaded(cover, ticketResult);
                    }
                } else {
                    if (mViewCallback != null) {
                        mViewCallback.onNetworkError();
                    }
                    currentState = State.ERROR;

                }

            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                if (mViewCallback != null) {
                    mViewCallback.onNetworkError();
                }
                currentState = State.ERROR;

            }
        });


    }

    @Override
    public void registerViewCallback(ITicketPageCallback callback) {
        if (currentState != State.NONE) {
            if (currentState == State.SUCCESS) {
                mViewCallback.onTicketLoaded(mCover, ticketResult);
            } else if (currentState == State.ERROR) {
                if (mViewCallback != null) {
                    mViewCallback.onNetworkError();
                }
            } else if (currentState == State.LOADING) {
                onTicketLoad();
            }
        }
        this.mViewCallback = callback;

    }

    private void onTicketLoad() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPageCallback callback) {
        mViewCallback = null;
    }
}
