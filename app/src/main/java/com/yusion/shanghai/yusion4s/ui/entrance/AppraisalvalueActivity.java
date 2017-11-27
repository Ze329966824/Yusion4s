package com.yusion.shanghai.yusion4s.ui.entrance;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.utils.Base64Util;

public class AppraisalvalueActivity extends AppCompatActivity {
    private ImageView appraisal_value_img;
    private TextView appraisal_download_tv;

    private String base64Str = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFQAAABUCAYAAAAcaxDBAAAAAXNSR0IArs4c6QAAIKdJREFUeAG9XQuwXlV1Xje5yc07uXm/QxJCAgQkPA" +
            "RlUFBQW9QZW3TUis60qLUVi5ShDmN9tNhptfVFdVCnnSoWUFSqpVYtCioiURIwBExAkpCEm9yQ5ObmdfO+/b717XX2Pv/97yOadsd79t5rr/Wttb69z/v82LJw9bd7W8ysF/9aWtgya8E/r1V5O/e" +
            "lTV0fTjbWm+05UI31ou0dwbgP6FIYcg5DUvgvdDXiGhEfR2nrEQR26YfYgvBt+HELDCT3NX+efwo8rLNdATZIs5XoTKbKDs0kQYNhJaJSINFnUCpqlAkQkNKWQEInyOiFodrwQrlruXLCg60GBMKh" +
            "FgdwfSoxUfmXFyqKhBQFBn0Eit5CHjWfiSmPBXjUddDKnP444JtkS6XBy7AKjEGkv5oZg2L0rlgb8WA5xMLqRBP7mO3Qc2XfIFiHxUZxZ/+eMIUoVCN4XwD36bFBaXD/1EiYxGWBiNKYod4T4qCul" +
            "vKn3hDKMOp4UGnW6FQJ0Fly4LJoJ30PpSFAd6gAZBu67kV+kr/Sp/sjlGeXbCBkV4SxLttJkbBNiutCXtpQLWMxFwhS+GUsffJPeVIn7InVXxlG/axczEaRIDQ0ixXpClZRpUBTjqKBYQEreVWt6N" +
            "n2vwgQnew/4dKOvpyRBJKSTz23CQeeqDNE7PBG/2qnITeV78JnzX+T/B0Cm3KviSCa1NjldUzz2LHxY413cnAeBHYHJuhtjrN4zmzn4Cl2HW8kPVQiLQbjOCpd+kwuFbcDJDpijP7dHHJvpgmSWNT" +
            "5gGJx60qPNvqTIvmhvXSz/yb5e46I0/07qmN5ME022OWVjOMjAJ9tBh7BsWZgBK4AcjJulxQq0hrss10FAPzwoTomMvuXbvTJdB2HyZGAArPmN9vLrlBMejlHEkxhPSZxk7xiPPuvY8mTtsMEqtkT" +
            "/zJ0Yyc1qSe0mKkASQsXXWKE1LuUVEEEMayzHidTRi5P5pS4ZQ0v6aWV6jMMvepEiOHAchj6cRzvVWPyn4HdxmOifvIRdowtVFPdmH8KX06w9ZOSemRdq7UKjKuiwHRwn5xMRIUEW+1GCItGMYkMxP" +
            "uwCWWMVT5Clmr3xw0Bkp4S9Q5kqFHq9tKV/4YxBxRc9t8Qv0Nyk/J1/+wq5souhYWRBv+UqLR6sOl6iyIGxT9hVlAC8LhTMPTvw9i4vnTLpKpLJgIDUHHLXmMkn4Ms2V7+2XcLjVLR/adk4LOaIVy" +
            "nBk7ln9ZJ6MgydwxORiBrYthzrYHz94hEsgMEcIXGFSocD5pyEuyxZx/oJ2dSkC5DSHIKAoayCJJYhS+0UzBJudp7AzGN0558eSAACDcUVR1vhtfwr8kKfR8VELVFpmNw0+KHC7bKEvFXZhhUPgzf" +
            "I6jUQ05BRNLqSXoCCpw2WTGtI0aoAYGhzZsXkaXAYlWFU2K4f+iR2BgngHOVzd0fx4dD9/RR420Z/ma0jrYJw0f437hhrXYcGPuOH7V9J47Z3hNHbeuRg7buyD7rONoDYiI8+XGPfncEJ4jDQwV+5" +
            "MWsPCYPRnKPjzF5/hxwK8U6YP46jkd+fuvp5k5AOQNqK4iQ50AUkPqVPRtRkglPGk5syKNO/laMbrerJsy0i8ZOsSVt42zksOGhMaR6L0hef3iv/ezATvvhvk7bcqynZhdkS8igSFSqMnvVpMZwW" +
            "i1pEgbKP8ExHzRb5q78Vi/ZpSPNkhxqbXLG0CricCsaFkK1GUJWDBnd5bZ8LBk53t46dYETOW3EKKqcsvLM4X32/b3b7Rvdm20PVrJHxbBS8VgaFo/iQ97MCWNM47fNv2UOCB1GHDh0vySwch5hsJ" +
            "bUeYxeIlVBSphto0VbWEP3wrHt9q5pp9vLx08vSNb4qd4eOnHc7u3eand0bbIOrFqRpjhEWp5+zz/x6aQyXo+5jKqf/Bsnh4S6aspf5AR6AOfjTKzkcCWCy0nIhwXXgcKitrH20Tnn2kvGTw2z/7f" +
            "6WO8JrNatdtvO9XYQbRbm6Md4ddJCQodp19gdQv6OQb1EeBAaJw6NY1D/Y7cqMctBqkOIUYi0O1M5RDwevmf66fbu6UtwbCwueSvEgRtHsMq6cIzsOn7EcPa09taRNmn4SBseDgY2r42+cOyQfXzH" +
            "Orv/QKcT5/klHObRJ3/IGt0MJX/s8t8kllv3AXDWRRTHeHhhCWARGjLIXV/9RTjB3L7oIluMM/ZQyvM4az96YLc9enC3rcLfdpy9e07EipLP8D8JZ/8FWPUXjZls5/tfu18NDMXP/fu224c712K1H" +
            "nd1z4EtJPg75e8QwJj9yDerk5J7SANsB3HV6tNUZnk68kYgEdyVE2faJ0+7wMYNbw3IpnX3sSP2ra4t9rXdm23jkQOVTvitJiyOQD6rnnuly8YwyEnum9vn2yvGz7DWloH3hmcP77cbtz1mmzGJ1U" +
            "rlbKUEsn8IKIu9z9v0nxSb5O+EMigp+VJlV9jJzseSw1iFwpRChQ+79848w26ctaxw6nC1zcZD++2LL/zG7tvTYYd9pTRbHQxCGci/ICJO1aVM7ekj2uyaSfPs7e2nYUJHSNhky8utm7c9bit7dvt" +
            "o4KbMm+cPzfDbX/5OqC7CmVQ++ZQkOW1JkDhOQbBKSaO+YfZS+wuQ2V/hmffznc/Yl0DmUezO9EdYnyvU8s86BV5OIt2gaEJ17InkJOcglXCIwr9praPsL6cvtasnzuZw03IY8byvY7WtPLjLfffJ" +
            "P60vd+3Y9J+LRNknB1tm/fwbvsuXScUSLw/UhMnA6bgKmexasDKX2E1zzsreGloPdHfah7ausW1HD9HKEwiVZokwWM8nJRJEeRAYoI3reFtIUk0J+qD5DcNHZp5t80eODXe1ugekXt+xCsfuLsfzS" +
            "UJSHlNjnLRMTuv+s8/681Do+52RJ6Pp0Z2SyKTECy+00IlJuHbaaQOS+bntT9t1G1Zax5F8F0PbwI49hICVLFxR0Qsvx8oiXZEoecbkngYZwvzlgV32lk0P2yO4k2pWRuNK5LbZ59vSNpw8G2Iig" +
            "MeTHGf/JDDn74ZJt2Xmw1yhHOamWHmUyA4jVYOK3vOZRPt8XKx/fellNqLJZRF3qZufe8z+E8dKxy5wtBKI1rhaid9iZ46e4DcA80aOsamtbbYf9/CdWN2rcCXwMMg54vSmCxSg+IoCi1o5ykWRcs" +
            "xsODD/asaZ9pb2BXTap/DZwFs2P2z709mfCk3zB6uUR/4kmd4o4yQ2EEoYlhxYrB4HT9YiuNem4ATw3bOusFkjR8us2JLMt//m5/aL/Tg+UZ6iUxt9D4y9OqG/N3GWvX/mMls4ahytmpaDIPeOXZ" +
            "vsX3Y+6w9LGE+Cr/TVT/iMwB2bvWvKYrt+2hmVXtl4cP8Ou6HjMYia5B+KDpVw4SQTKgctMx6+h89lpJ6UGYyzncSTQdwdZ7wkQSYhelNGjLR5uB5sLHwg8p4Nv7AfdG934gp4n1k/NkPofmBM/+O" +
            "w630al1qXT5jRCNdvfwdW7Ps2r7I1h/ZUOrFyKHBSPWPFzC1lt8w4C5dYCyqbsrH+0F476jRl6dajB+2W7Wt8Uhw/sSje6guiyYUij01KNiAPHjtq542bHN1B6w9vWWPf3wMyG1Yfs2EsXpAYJ41" +
            "kT8Tlzd1LLrUlQ7wJSAg2HQ9WvrLwEnvfltX2Y6wuJhh7FHW0KEhj8pr8/13nU34V8EpcszaWpaMmNIrsSNyyMtiAIioc+KSxjT96SiclaXmCSZHK0T+ESxyeDYdS7nxhk31lx0Z3FvrEYhFmtD" +
            "XKY9ttWJknS6aszR/3/ePcFbYYd2byI1IjdqaqNuUpBhjf3PG4bcQF/lBKN259GXXErzYEZBAb7pEsHMcT+9yp5jIphhI1uo4eZnfAsg1n8Y9tWSsdx9DD1z5GfpUgv2+dcpq9dPy0PionI+Ad2d" +
            "/OPrduEv5191ofg+sjWCQf2r62IqOuUO914yZAJe9hmCYXVXXiEW89KU/e0WK/mgkf1O7fhdvEwcoHNj3uT9V9pThOWOTVQYkHgX1lFG4Rr8ed1akoK8a0G3dhxU4nXJkIIqUWOVEg/2aP49rzzq7" +
            "nBnXf7c9VxQuVHZc4iasM0BJvPcWzFOlSUej4oMDW9ezNdk1a39m11X7Ek5Db5gniRAZuaUbZFTgB8UrhVJU3tM9tgEIctYnNwySD5bM7nvYHMeo13z59aJ/Iqw0ngJDRD/75x2IBHipMlm2+Pt" +
            "BYiz26b1eYNq1v61jvwXOQ1o6RcMhxkBo1HbwCD1FOZXnpuGk2AquesTNu5cFeo38PyMf51OmO3Ztcp7/NE4e6+8Qf+LQRR2zwQU1CiUS9Tqeuagyaq/fpIUJSr1UP7um0X/d0u4z2YVc7BWJUqXF" +
            "X0YQtHuBas+ZgiB3e9czETUD2XzeUf5HrbeRJ3W/s2eKHqrq2entwQnruaH4S5oeMxKD8aBv8+bdNSq8KQwmnpF0R3p862N3vmf72bU/nWABDf+6TGP6Xhl3OVOir1y97suGpaU0fMbqKnzMo/8l" +
            "niic8+RiUDuBG4R6Q2qw80YNrXJhzjZGn4Ir5EbXCT/QVJ6VInmpFARJBjgGBzy8bCwHX96RjDAOuvWznChB5DaiAaTHeTZ3qcuh4HTPOB0FEoz8Sw1iewQV9s7KLl0zMKxKIGsqlnOPs+5NYNrR" +
            "qCKmzV6LflSibMbLNZja5xeSdwx/PWAwNzZgu3hl++hfYqOUnAum1HUO4FGNEJ1N24FUHs6n+3L8Edf8ZlfK3TV6YBUXrnFETkb4WVWlPD7W7MrlIhLJTJO54YIhBecHYBeP6f8F27YyFNgrHL" +
            "5ZwypWRcyknTLik+zE86DiVhQ9P+OqERf6VQZVHcsa+T3cK8EJ8G3D2aBDXpCwaOc7GtTA3Lhn+qdA0cmUdPvzru1BzBT9YULswAMYFA9x6TsLLs7fiEV4Ud8YOGwRPWKhEMuRM+Ad7tlHrlJUf4" +
            "n08HSpBOiU0AmDC/OM/D4JSBibdP5m6mIpNC1fhcqxS58YxCAuQRJrawiFA9Tw00ERqMkoOObZiXHuoNK1vmLPMJuONZBThwBGOqZGXGtKg7Je4FFtzoCtMfqf6OJK944WNTp6IApxzGf7ZyS4oZ" +
            "YyXjZtuL8N3AgOV5aMnOX/ZXFicHOeVjcRV8X0oY8kmauZ+s+NnGQSfSH1s4XlaAWmAeIEQ2KWMQdyKW9UYK/FOtn0PXvTxq5Hw50sCnejLR6xeoY/BYeojs5YP6mo6LsWCGseJDizZVFe5+kmp" +
            "QuTBFwonqjO1+pS141HdYOX1U+baa9pnASPSSBbeB1YBEDqP7Ntpn+5YV4ycfJOXdH/X8SSmx9dLg394TY7z9JKEXrsJD5ybPcttjGAi9zxCVwloYqhXidw7bj3phEI68CRhyONGyGhELD5iG0" +
            "r5+KIVNhfPSIMw2lRYieg8Jt+fwl3Wlzs3DAW+j8563BK/a+NKO3j8WN2nZ4qN/oexFAgrdK4cP9PePHl+H7xmgonDRriNok1cOU5skgy4fgytgUBI55prjZDM8hKhpt/QmYpnlHcuuxRfeXA3Y" +
            "RZFSRMVkmoYzv56868sHq7E+GD1t3dvtWue/inO7Id90hh16dPx4dNZpQaajOh8PEj5p3krhpwTP6yQeQIAhlBzftFqaf/x19DWWZcJ0D8HacAt+yNxf/winpSE4jWbZ4yZYJ9cfEHTwHjv/6anf" +
            "mJMlUmWE+I5ElsuFGxKnJ/b/CneoL5+8lyb0zaGGrXSg7uaH3fvsC/gdfSvcLtbhZQa8qMMCOl7W+F/MfaeuxZfarwyaSx8onbDllX+aC+CIwZvQNbh+EwXXiD0NvHxjyvX/dJP+4N3i9w0GFbJB" +
            "PYYTgBhxL5PAuoPzl9u189dJkcN24eQ+DuffsT4PFGJSiFwShn9ElXYavOh83wQMA0nvHhJ98SBPXhBF5/ouIWDRkuYeQJjcVB+Jp7Gf2HhRTYTt6eNhQ+J37npF/bwQb0dreWPyJgyMfLiyD6IV" +
            "fmf9MDd+hQHnySLbQ4nZedSYJSWJcjgWe2by19ml05sfumxAbel71j/c3sWX4tUuGi5PZJg8TYDRlsSttXjmHzlkRKnsnecDEACWGRr9io82foEdvMx/Xwe9JnO9XY7PsCQF3ER9oHlgGkTuG" +
            "UsjDmd5XXcjOOPkmEwtNYuS0eRkjDVOwHpdesfsXU40zYri0aPt/uWX25XTKq/vwlfYcN+4tep5CaC9jEoZv+JrTBm7fZZQ0PIBKI/w9d//7zgwn7JvBffV31+xzO++mh3MvnLD7bw47v+RK5Qo" +
            "fiYJ8HMuDKSNmWunCTljKmNT19wr3/v2S/342oy61P9x84t9tHn1tiOY/XXKfLPbd4zwjml8s9EVUS0yFNbOiS11D0PJ5+/mXeunT2m+W0l0b7TtdVu3vK4fniMvuP9FvlXsXGXZ6RcBbXgCE6PL" +
            "KlBiklg6AaZUuq1GTg23XvOy+10rMr+yn4cTz+x5SlcJm3E61p830RFx0zJsFv4I8nJqeLjWBJFvIRgCayp+K7pJnxj9cYp82Wj4T7b/8IHGDdtXu1HZF8wyXHgpjAE7D4hcf/iSv5CizTi38QH" +
            "7tJUsxNjyXXMFmv+0SlLH4cuk5yXWF8842K7vH3gp/G7cKlzN96QfhVvSDcfxmeFBa4zFv2CvBRoFafCTVtUL8YPH/4IzxReM2n2gB/4ckFwF//MdrxlqHIeLP+IiibZpwfusUreMuFHdw3+fSi" +
            "IdJDkPAjlLsbsIiavseH3mh9ccI69d87S8NdvzbPrT3E1wI/JVuKuaS3O4unboIo4GgtbngDvZTSeAvGhzcX41PzVk2bZUny+M1g5gBuAm7c8po8wkrLyieliSuEnOWrMn9FwiPmjEfFQ5IRq3D" +
            "VcARtXqiRuIYfuTDy6Y0FKX/xWVvaaybPs73HnNLvJ9SR9NCs8JKze32UdWLW78XB3N1ayfxKOa+HJuHbkSz0+hFmES6rlYycO+nFt6eNRfBZ0y5Zf2Qa+j0dOjDRKJlVSbpm25zdQ/sEF9F2XKz" +
            "STQhj1HCN5cxdJ4O2Qa8BtYlY5RFWGS6SxeADxgQXL7bpZp/9W38YT73cte3DB/g8dT9nXd2322IhX5ly2q72uSFRpSlCIExYlxfnnlqdX9355Gz66wq7AEjPFOk4+PsCxNB4HcEFpFw9dklmtV" +
            "DT0qwqzs3CmvXHemXb1lDl+SAjM/8uaq/2unc/5XdVuvr5BcIyPbOYFkMlgLFX+viAaxtJ4s/zH4PDzJnyS3nLs2PFekvkVkHr788/Y89jV3CnRnUKBBnkRCPsslS4bJLAS1INzKOhzV/3zOUvs" +
            "mmkLqqf8DnQKN/yI7N92bLCv7tzoC8Vj9bgQXwSSEvA+2qnrUeQUKB04/1l4dnHtlIX2xvZ5/gm6E5q4MD6k/TauFf8dlzQP7sbPT8KpKwA7+mxmr/UgIK8Fx1HXTWSn/Yvfv1+JK4GrJ8/xur8" +
            "7GAcfwqYTnwF9D28A/rurwz+h5A0HA/ZYIm7Pw5dnXgjqum5FHvS1l8mx55ripoR3Qy/BNwB/CBJfjTuw8kcSTiiVaMTdNspWrNS7OzfZXds32abDB3IA1PUwRZD7QQCZRmGUhDufSk3bhgRHDx9" +
            "u5+DD3eVjJ6Ge5PVMzPyE1hHWlt5VRVx8TMdnA88dOmBPHtxja3GH9iT++BliWbLPNJFp0F37YJ7gSjfFRVU2gxPW1OHHv2+YNNfeACJnN3lh6Xbc5dlgoVFwGmCU88nRd3c9b/ftfN42+D25HI" +
            "ZBnTyAMBoAhTxwKXbcpruRT41DcnL8OIW6DcYT8XCbr7H34rPKEwShPZ24H2JSpuI+G+TCk7rHAlXqSS3HSYQsl/5peDhzFT4CvgqfDfHOy/Hlquk27fJ5ddIgTjBlHdbP9Oy37+7cavd3bQPRu5" +
            "Gofs0R46yVoMItA8jtvDqcF8+yb2IkTmkJMyiQj0y6cEO3PxxiZOa9zdUDmYuT+Uh0zsVvnq6YMN34eyv+3JwluPDOAJtil/dF5ap0kHxVNRPLoPLeg93vlyD1Z7gw/1n3C7YKKxnPzSseGD4Dz3" +
            "aKJBLz9FKOkaDrFyswSAybyEW2McorjXDLkURUU/+ZcGrytwEvGjPJLsHNwcV4VX7B2Mm1k2Vj7OG/v7oitFTwpMhoKtEPoilWgtgxFbtr8ptLfkWyBhfmvON5Am80n9zf7Z+6lIR4G4ZxSeX7HjEF" +
            "7Fje9pYGSB39l6WGyQGMB8XlmA/BmC/l+GMIHqv5Hv5sEMlnriMcmOCZ7DIv2kd/MIIrQhkAlctSyqKtpMpVR5sy0yA5ZL227cgh24STyCYcLjbjJ4ist+KsvBOXNztxJ8SHxyxKyZvVhInIYoQBcD" +
            "IUiOxSe0Jrq00Z3uZ3U3Nxd8bfgy4YhT/UfFDNE51T7hDCaCRIFGT8xnFF1/+2IjRUSsJKWXDdH7ExGTnRILqsiRjksGbhd1Nmu/FIb9fRIyD3KF5BHDd+hn4Ux2eu+vjGna9iRg7HH34k04a6Db" +
            "vrOLxA89tR3Ja2evAZV/jchk+0oKNYteKjzThcM01OEJlrjmaiXbnJxgnNTrJGyKLmCNss2UkZRElcmVS0aU9bh0ht2ZRyjWbd5NLt5D/vAYqDFvSRgAOgkuUx5UKFvDrZC3nEUfdJjeaFuYRuaP" +
            "gTewbWWBplcsZkGEyQGsGIGNnECpDsA8+utj9Y8yCeOYoIbLH6jlsPvpLjKuSPZ9lmfQi7vn5Mm4lnXDnwINOlVTLZL75G2bvbXrn6f+xLuOurl3pcYSMy6zlxLPtUroElO/UayaS0z89qsgMF" +
            "kAGa7SKcfc0S9cIBMaK04R73ga5O+/TmdXbj/DPt0b277FWP/TCGm9ZrL3mdzR2lN55MrFnhXd3j+/j7pLoC5Zt6DtinNv/aVoyf7OtU9jn+GW2j8e3A6LQwNKo8I58cfymnZpmbLOvbPsfQ+r" +
            "B6QTJ7Wp3lygwn5SrNekeQ4GWrvudJ/uiCK/H6ts3+teM3zdzYQ3t2YIXtsrWXvBavkPWDsmJuqlVDkvnz7AUPfaspzmDC6+cts1tP52dDmgzlFzHHBJFUHRoCT4uGC026lIcsdPqs0BhgHUTKi" +
            "MYuxSYCCZlq/pdoYwY/u2WdE/eDFVfa55e+2D668Qk8KzCsjDH2oYXnEigV2dDHrZvWOqHw7BNXaRQJaEz+Of7iCVPtTTNOS4nJgoeXo/iciPfYw6vFlgk6Zxy/pqOuZLKK3bwkTMZBYORWTnLI" +
            "hDHALk8FJskSxLIdM5LH6jKt4F7jM0jeb/NO6kIk/Z1zL09kE7NMRAlIRg8qybV38l4RvlhLb+nYCXbdHH2OqMRbMCk77arV99uHF51r78dhpu4PPQBG/BxrzAnokOUYQ1817SPGvIAkaUJodpQd" +
            "N5MJVMhKOMCjDhdZR5IINMtFTmIIShGwcAMnWcNMxIVcOFkWE4bDwrFjePLPD3DrPvk19nCs3vDDceWoySVxyVs4qWrFpByzXjVcPynFTMRwMyIpy4nmdhVDCl4kaUVxxpUUkcu2yJG/SCJkeZX" +
            "Eb08DM/sSXiYzMDQ5n9r8FE5OTwm+2D568dV4M8ufMlKYyQ7cyDvzwUOQsJV/+ClAU7N2DA2gUBOIji0BJJ18xlQyMcNKhHYRXAQSmPVa+plsjco2MLRqOEJ5kMBDCQsv9sNXY/xX4HnrSyfxi5" +
            "bST69NxQrNRGq10VbY9FP6jNxEYplPcMI4otQIDWHUEWAkIgAmpZUpx5lEBU7HOThhSCbcaLPmn0qQwl4kFu3wz77avbY/vbIZj2emKpyAIE6Sl7VPb3IMDf9BnGINEgM/MJVrEBzkK46S3BR" +
            "EfZcPIeuSLCaY+wpISccun0mNAIKgqIFQwEdSIWNS0ZaaEssmZZ9tPhtlGY9vlbrR5gMZFsYVnwXxmcFPcSmmIp8X4QQ5yv/rEzHpkjfmmP0pLvYjB42JXEojZ7b7XaFaWZnI6HP1scTsBJjGY" +
            "wal44rFMTUHxXEFSrw8WbLI2xjLEraYPN99sfAV9boDe+11jz/g/XLz5W0bjH9leQzXuAvxwEQ+ORI+ypUa8cWY9LiNWCP/kLFm6ZdQDTN4ghNIiYgIynIAZVDRdiNquX0cD4VVksm2XATBsqQ" +
            "sJ802sQLVbCNWH8uSMeP9XRjbb5u50H5/6hw2+5Q78SrnPjwYjyIsAsp/EFX6DZnIizgVF3GkG4iqByWUamUyQTADIXm5r2y1CnPy0aeuguesO6r3Q05JlLAJAtmP4GOCVu/FfxYIBkvw0e+v8" +
            "cyV5Uw85xShis2Fvmmxlbg2VclxCl/98KW8qMncPFA3o98gmDqMSXFJ7hbQSZ8zuk2fjWYmksngjUDUi78Apo7avsVGAmGyrUSynHoqMUnCiD0hx0H5Q/jBLu/V+V/BDV9Rhz2xM2kpIHdBuS" +
            "ZYHsv4tUiUT8RD8nL8bAo3y6lJnaYrlAN0mAMjQMxsBEOIIEVJB2jpkDImRl1CKLAymV7rPHIYzz6P22rcx7O0pY9ic9A5AY7/pGsHHlr32LWzFrFblc9tWW/34E1t+KsG0HjeL/ApUSzlGNt" +
            "lftHPJNbHqRtj9XY/x9AAb3QaRFNetiNIEaldmu3QU0s20VYtYm/duMa+un2ji/gpJP87TTEp8iPtwOzEGwD+Jy/fVhEqZ9ziqlTKvvPFxCGmJM0VJRxnXDFhsVi0YoMsTax2czdIaDEumXCa" +
            "rtBQYJ2d5VmiPEgPYhvr8/Ajh3cg4bFYbdlxuUodnRt799wltgyf6vAVxtVT50FCvZICEaPEeu2a6fP9w9756RHf9JGjHOO1+J3UZbj2pF6Qj5b7/97uDnuwa3vx8yCRGeMRP/vyz8giBq3I8E" +
            "9dH011+PM8y/fyrjXAJpwGyQLOu3t2IgLYL4kXdIxFLSmTYAIZm7YxhpGUWw6+LqOmdEQIsYKYLKdW+K2TxBEW5RhYjEFBBImxOKKWVd6e9PPQMA1y2Y921KGjMd9ik1dnDibLsk2wmIkJkgO" +
            "vJDVkYR+TEDaZzMANzcB3BGyC4L56gVXqKFfayo4tlt" +
            "a1+7sPIy3+nxJQhzOCLXaT6qtzaHHM1TGCF+AkpCwlkZVitbvQPpzSLiF5xQ3vyXmxUYyhx8J37fyVJLUiqbKmDkNxGTso2b/6LsvNAVr00jcGN/Ahd0QFiXifq7xcBhUm2fu/AEf41D4CJU8AAA" +
            "AASUVORK5CYII=";

    private String str1 = "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";
//  private String path = "C:\\Users\\LX\\Desktop\\";
//    private String imageName = "874427913279298799.jpg";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisalvalue);
        initView();
    }

    private void initView() {
        appraisal_value_img = (ImageView) findViewById(R.id.appraisal_value_img);
        appraisal_download_tv = (TextView) findViewById(R.id.appraisal_download_tv);
        Log.e("TAG", "base64 = "+getIntent().getStringExtra("guess_img"));
//        Bitmap bitmap = Base64Util.stringtoBitmap(getIntent().getStringExtra("guess_img"));
//        CheApi.getChePriceAndImage(this, new OnItemDataCallBack<GetChePriceAndImageResp>() {
//            @Override
//            public void onItemDataCallBack(GetChePriceAndImageResp data) {
//
//                Bitmap bitmap = Base64Util.stringtoBitmap(data.result.img);
//                appraisal_value_img.setImageBitmap(bitmap);
//            }
//        });
        Bitmap bitmap = Base64Util.stringtoBitmap(getIntent().getStringExtra("guess_img"));
        appraisal_value_img.setImageBitmap(bitmap);

    }
}


