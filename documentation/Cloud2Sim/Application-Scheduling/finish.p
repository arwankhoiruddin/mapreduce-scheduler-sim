# This is to set the colors of the plot
set term png background "white"
set key below 
set boxwidth 0.5
set output "finish.png"

set xlabel "Scheduling Algorithm"
set ylabel "Start and Finish Time/ms"
set yrange [-15:3000]


plot "TO.dat" u (3)-2:($7/4000) smooth frequency with boxes title 'Finish Time: RR-O' fill solid 0.05 border rgb 'grey30', "TO.dat" u (3)-2:($6/4000) smooth frequency with boxes title 'Start Time: RR-O' fill solid 1.0, "SO.dat" u (3)-1:($7/4000) smooth frequency with boxes title 'Finish Time: FCFS-O', "SO.dat" u (3)-1:($6/4000) smooth frequency with boxes title 'Start Time: FCFS-O' fill solid 1.0, "SS.dat" u (3)+0:($7/4000) smooth frequency with boxes title 'Finish Time: FCFS-FCFS', "SS.dat" u (3)+0:($6/4000) smooth frequency with boxes title 'Start Time: FCFS-FCFS' fill solid 1.0, "ST.dat" u (3)+1:($7/4000) smooth frequency with boxes title 'Finish Time: FCFS-RR', "ST.dat" u (3)+1:($6/4000) smooth frequency with boxes title 'Start Time: FCFS-RR' fill solid 1.0, "TT.dat" u (3)+2:($7/4000) smooth frequency with boxes title 'Finish Time: RR-RR', "TT.dat" u (3)+2:($6/4000) smooth frequency with boxes title 'Start Time: RR-RR' fill solid 1.0, "TS.dat" u (3)+3:($7/4000) smooth frequency with boxes title 'Finish Time: RR-FCFS', "TS.dat" u (3)+3:($6/4000) smooth frequency with boxes title 'Start Time: RR-FCFS' fill solid 1.0, "DT.dat" u (3)+4:($7/4000) smooth frequency with boxes title 'Finish Time: D-RR', "DT.dat" u (3)+4:($6/4000) smooth frequency with boxes title 'Start Time: D-RR' fill solid 1.0, "DS.dat" u (3)+5:($7/4000) smooth frequency with boxes title 'Finish Time: D-FCFS', "DS.dat" u (3)+5:($6/4000) smooth frequency with boxes title 'Start Time: D-FCFS' fill solid 1.0, "RR.dat" u 2:($7/4000) smooth frequency with boxes title 'Finish Time: RR', "RR.dat" u 2:($6/4000) smooth frequency with boxes title 'Start Time: RR' fill solid 1.0
