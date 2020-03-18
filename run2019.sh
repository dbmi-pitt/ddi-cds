start=2018-01-01
end=2018-06-30
mkdir custom-run

while ! [[ $start > $end ]]; do
    echo INFO: Running engine on data extracted for $start;
    echo INFO: Output for this run will be written to custom-run/output-$start/idia-rules-run-$start.txt;
    mkdir custom-run/output-$start;
    java -Xmx16384m -jar target/droolstest-1.0.jar $start ${@:2} > custom-run/output-$start/idia-rules-run-$start.txt
    sleep 1
    start=$(gdate -d "$start + 1 day" +%F)
done
echo INFO: run completed. Assembling the resulting data and writing it to custom-run/full-custom-run.tsv
grep -r "^DATA" custom-run/output* | sort | gcut --complement -f1 > custom-run/full-custom-run.tsv
echo INFO: Assembling the unique resulting data and writing it to custom-run/uniq-custom-run.tsv
grep -r "^DATA" custom-run/output* | sort | uniq | gcut --complement -f1 > custom-run/uniq-custom-run.tsv
echo INFO: All done!
