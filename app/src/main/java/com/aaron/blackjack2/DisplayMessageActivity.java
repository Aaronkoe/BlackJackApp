package com.aaron.blackjack2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DisplayMessageActivity extends AppCompatActivity {
    int numPlayerCards;
    DeckOfCards deck = new DeckOfCards();
    Card dealerCards[] = new Card[9];
    int numDealerCards = 2;
    Card playerCards[] = new Card[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent creator = getIntent();
        TextView tv = findViewById(R.id.textView2);
        tv.setText("Num players: " + creator.getStringExtra(MainActivity.NUM_PLAYERS));
        ImageView iv = findViewById(R.id.DealerFaceDown);
        dealerCards[0] = deck.DealCard();
        iv.setImageResource(R.drawable.card_back);
        iv = findViewById(R.id.DealerCard1);
        dealerCards[1] = deck.DealCard();
        iv.setImageResource(getResources().getIdentifier(deck.CardToFilename(dealerCards[1]), "drawable", iv.getContext().getPackageName()));
        playerCards[0] = deck.DealCard();
        playerCards[1] = deck.DealCard();
        iv = findViewById(R.id.PlayerCard1);
        iv.setImageResource(getResources().getIdentifier(deck.CardToFilename(playerCards[0]), "drawable", iv.getContext().getPackageName()));
        iv = findViewById(R.id.PlayerCard2);
        iv.setImageResource(getResources().getIdentifier(deck.CardToFilename(playerCards[1]), "drawable", iv.getContext().getPackageName()));
        findViewById(R.id.PlayerCard3).setVisibility(View.INVISIBLE);
        findViewById(R.id.PlayerCard4).setVisibility(View.INVISIBLE);
        findViewById(R.id.PlayerCard5).setVisibility(View.INVISIBLE);
        findViewById(R.id.PlayerCard6).setVisibility(View.INVISIBLE);
        findViewById(R.id.PlayerCard7).setVisibility(View.INVISIBLE);
        findViewById(R.id.DealerCard3).setVisibility(View.INVISIBLE);
        findViewById(R.id.DealerCard4).setVisibility(View.INVISIBLE);
        findViewById(R.id.DealerCard5).setVisibility(View.INVISIBLE);
        findViewById(R.id.DealerCard6).setVisibility(View.INVISIBLE);
        numPlayerCards = 2;
        tv = findViewById(R.id.textView4);
        tv.setText("Player Score: " + ScoreCards(playerCards, numPlayerCards));
        tv = findViewById(R.id.textView5);
        tv.setText("Dealer Score: ?");
    }

    public void HitPlayer(View view) {
        if (ScoreCards(playerCards, numPlayerCards) > 21) return;
        playerCards[numPlayerCards] = deck.DealCard();
        numPlayerCards++;
        String nextCardName = "PlayerCard" + numPlayerCards;
        ImageView iv = findViewById(getResources().getIdentifier("PlayerCard" + numPlayerCards, "id", view.getContext().getPackageName()));
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(getResources().getIdentifier(deck.CardToFilename(playerCards[numPlayerCards-1]), "drawable", view.getContext().getPackageName()));
        ((TextView)findViewById(R.id.textView4)).setText("Player Score: " + ScoreCards(playerCards, numPlayerCards));
    }

    public void Stand(View view) {
        ImageView iv = findViewById(R.id.DealerFaceDown);
        iv.setImageResource(getResources().getIdentifier(deck.CardToFilename(dealerCards[0]), "drawable", iv.getContext().getPackageName()));
        while (ScoreCards(dealerCards, numDealerCards) < 17) {
            dealerCards[numDealerCards] = deck.DealCard();
            iv = findViewById(getResources().getIdentifier("DealerCard" + (numDealerCards + 1), "id", iv.getContext().getPackageName()));
            iv.setImageResource(getResources().getIdentifier(deck.CardToFilename(dealerCards[numDealerCards - 1]), "drawable", iv.getContext().getPackageName()));
            iv.setVisibility(View.VISIBLE);
            numDealerCards++;
        }
        ((TextView)findViewById(R.id.textView5)).setText("Dealer Score: " + ScoreCards(dealerCards, numDealerCards));
    }

    int ScoreCards(Card cards[], int numCards) {
        int numAces = 0;
        int score = 0;
        for (int i = 0; i < numCards; ++i) {
            if (cards[i].rank == 0) {
                numAces++;
                score += 11;
            } else if (cards[i].rank < 10) {
                score += cards[i].rank + 1;
            } else {
                score += 10;
            }
        }
        while (score > 21 && numAces > 0) {
            score -= 10;
            numAces--;
        }
        return score;
    }

    private class Card {
        Card(int r, int s) {rank = r; suit = s;}
        int rank;
        int suit;
    }

    private class DeckOfCards {
        private int deckArray[];
        private int nextCard;
        private Random rng;
        DeckOfCards() {
            rng = new Random();
            nextCard = 0;
            deckArray = new int[52];
            ShuffleDeck();
        }

        Card DealCard() {
            if (nextCard >= 52)
                ShuffleDeck();
            int cardNum = deckArray[nextCard++];
            return new Card(cardNum % 13, cardNum / 13);
        }

        String CardToFilename(Card card) {
            String cardSuit = null, cardRank = null;
            int cardRanknum = card.rank;
            switch (cardRanknum) {
                case 0:
                    cardRank = "ace"; break;
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9:
                    cardRank = "" + (cardRanknum + 1); break;
                case 10:
                    cardRank = "jack"; break;
                case 11:
                    cardRank = "queen"; break;
                case 12:
                    cardRank = "king"; break;
            }
            switch (card.suit) {
                case 0:
                    cardSuit = "clubs"; break;
                case 1:
                    cardSuit = "spades"; break;
                case 2:
                    cardSuit = "hearts"; break;
                case 3:
                    cardSuit = "diamonds"; break;
                default:
                    break;
            }
            String toRet = "card_" + cardRank + "_of_" + cardSuit;
            return toRet;
        }

        private void ShuffleDeck() {
            nextCard = 0;
            for (int i = 0; i < 52; ++i) {
                deckArray[i] = i;
            }
            for (int i = 51; i >= 0; --i) {
                int index = rng.nextInt(i + 1);
                int temp = deckArray[index];
                deckArray[index] = deckArray[i];
                deckArray[i] = temp;
            }
        }
    }

}
