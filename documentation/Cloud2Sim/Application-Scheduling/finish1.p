# This is to set the colors of the plot
set term png background "white"
set key below 
set boxwidth 0.5
set output "finish.png"

set xlabel "Scheduling Algorithm"
set ylabel "Time/ms"
set style fill pattern

set yrange [-15:3000]
set xrange [0:7]
set xtics ("1" 1,"2" 2, "3" 3,"4" 4,"5" 5,"6" 6)

plot "TO.dat" u (3)-2:($7/4000) smooth frequency with boxes title '1. RR, VM o/sub - MET' fill solid 0.05 border rgb 'grey30', "TO.dat" u (3)-2:($6/4000) smooth frequency with boxes title '1. RR, VM o/sub - MST' fillstyle solid 1.0, "SO.dat" u (3)-1:($7/4000) smooth frequency with boxes title '2. FCFS, VM o/sub - MET' fill solid 0.05 border rgb 'grey30', "SO.dat" u (3)-1:($6/4000) smooth frequency with boxes title '2. FCFS, VM o/sub - MST' fill solid 1.0, "SS.dat" u (3)+0:($7/4000) smooth frequency with boxes title '3.FCFS - MET' fill solid 0.05 border rgb 'grey30', "SS.dat" u (3)+0:($6/4000) smooth frequency with boxes title '3. FCFS - MST' fill solid 1.0, "TT.dat" u (3)+1:($7/4000) smooth frequency with boxes title '4. Max. Util - MET' fill solid 0.05 border rgb 'grey30', "TT.dat" u (3)+1:($6/4000) smooth frequency with boxes title '4. Max. Util - MST' fill solid 1.0, "D.dat" u (3)+2:($7/4000) smooth frequency with boxes title '5. Dyn Alloc - MET' fill solid 0.05 border rgb 'grey30', "D.dat" u (3)+2:($6/4000) smooth frequency with boxes title '5. Dyn Alloc - MST' fill solid 1.0, "RR.dat" u 2:($7/4000) smooth frequency with boxes title '6. RR - MET' fill solid 0.05 border rgb 'grey30', "RR.dat" u 2:($6/4000) smooth frequency with boxes title '6. RR - MST' fill solid 1.0
