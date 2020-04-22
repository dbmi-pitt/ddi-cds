INSERT INTO simulated.f_observation_view 
(observation_id, observation_date, person_id, value_as_number, unit_concept_id, observation_concept_id, observation_type_concept_id)
SELECT m.measurement_id, m.measurement_date, m.person_id, m.value_as_number, m.unit_concept_id, m.measurement_concept_id, m.measurement_type_concept_id 
FROM simulated.measurement m;
