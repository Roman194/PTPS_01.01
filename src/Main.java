import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        int candidatesCount = sc.nextInt();
        sc.nextLine();
        List<Candidate> candidates = new ArrayList<>();
        for(int i = 0; i < candidatesCount; i++){
            candidates.add(new Candidate(sc.nextLine(), 0));
        }

        System.out.println(candidates);


        List <String> votes = new ArrayList<>();

        while(true){
            String currentVote = sc.nextLine();
            if (currentVote.isEmpty()){
                break;
            }
            votes.add(currentVote);

        }
        System.out.println(votes);

        List<Integer> eliminatedCandidatesNumbers = new ArrayList<>();

        for(String vote: votes){ //1 подсчёт результатов
            String [] detailedVote = vote.split(" ");
            Candidate currentCand = candidates.get(Integer.parseInt(detailedVote[0]) - 1);
            candidates.set(Integer.parseInt(detailedVote[0]) - 1,
                    new Candidate(
                            currentCand.name(),
                            currentCand.score() + 1));
        }
        System.out.println(candidates);

        boolean winnerDetected = false;
        while(!winnerDetected){
            int minScore = 100;
            for(int i = 0; i < candidates.size(); i++){ //подсчёт процентов и лучшего/худшего кандидата на итерации
                Candidate currentCand = candidates.get(i);
                double percentScore = (double) currentCand.score() / votes.size();
                System.out.println(percentScore);
                if(percentScore * 100 > 50){
                    System.out.println(currentCand.name());
                    winnerDetected = true;
                    break;
                }else{
                    if(!eliminatedCandidatesNumbers.contains(i + 1)){
                        if(minScore > currentCand.score()){
                            minScore = currentCand.score();
                        }
                    }
                }
            }

            if(winnerDetected) break;

            TimeUnit.SECONDS.sleep(1);

            List<Integer> eliminatedOnThisIteration = new ArrayList<>();
            for (int i = 0; i < candidatesCount; i++) { //внесение худших в реестр выбивших
                if (candidates.get(i).score() == minScore) {
                    eliminatedCandidatesNumbers.add(i + 1);
                    eliminatedOnThisIteration.add(i + 1);
                }
            }
            System.out.println(eliminatedCandidatesNumbers);

            if (eliminatedCandidatesNumbers.size() == candidatesCount) { //если все выбыли (одинаковый процент у всех оставшихся)
                int j = 1;
                for (Candidate cand : candidates) {
                    if (eliminatedOnThisIteration.contains(j))
                        System.out.println(cand.name());


                    j++;
                }
                break;
            }

            for(int i = 0; i < candidatesCount; i++){
                if(eliminatedCandidatesNumbers.contains(i + 1)) {

                    if (candidates.get(i).score() != -1) { //"зачёркивание" кандидата

                        Candidate eliminatedCand = candidates.get(i);
                        candidates.set(i,
                                new Candidate(
                                        eliminatedCand.name(),
                                        0));
                    }
                }
            }

            for (String vote : votes) { //перераспределение голосов за оставшихся кандидатов
                String[] detailedVote = vote.split(" ");
                if(eliminatedCandidatesNumbers.contains(Integer.parseInt(detailedVote[0]))) {
                    for (int z = 1; z < detailedVote.length; z++) {
                        if (!eliminatedCandidatesNumbers.contains(Integer.parseInt(detailedVote[z]))) {
                            Candidate currentCand = candidates.get(Integer.parseInt(detailedVote[z]) - 1);
                            candidates.set(Integer.parseInt(detailedVote[z]) - 1,
                                    new Candidate(
                                            currentCand.name(),
                                            currentCand.score() + 1));
                            break;
                        }
                    }
                }
            }
            System.out.println(candidates);
        }
    }

}