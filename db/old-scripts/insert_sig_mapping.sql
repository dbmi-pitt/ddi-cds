-- Drop table

-- DROP TABLE sig_mapping;

CREATE TABLE sig_mapping (
  id serial NOT NULL,
  sig varchar(200) NULL,
  expected int4 NULL,
  min int4 NULL,
  max int4 NULL,
  CONSTRAINT sig_mapping_pkey PRIMARY KEY (id)
);

-- Permissions

-- ALTER TABLE sig_mapping OWNER TO postgres;
-- GRANT ALL ON TABLE sig_mapping TO postgres;

INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('2 times daily',2,2,2)
,('2 times daily before meals',2,2,2)
,('2 times daily PRN',0,0,2)
,('2 times daily with meals',2,2,2)
,('3 times daily',3,3,3)
,('3 times daily before meals',3,3,3)
,('3 times daily before meals PRN',0,0,3)
,('3 times daily PRN',0,0,3)
,('3 times daily with meals',3,3,3)
,('30 min pre-op',1,1,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('4 times daily',4,4,4)
,('4 times daily after meals and nightly',4,4,4)
,('4 times daily before meals and nightly',4,4,4)
,('4 times daily PRN',0,0,4)
,('4 times daily while awake',4,4,4)
,('5 times daily',5,5,5)
,('60 min pre-op',1,1,1)
,('After breakfast',1,1,1)
,('After breakfast and dinner',2,2,2)
,('After dialysis',0,0,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('After Every Dialysis',0,0,1)
,('At bedside',1,1,1)
,('Before breakfast',1,1,1)
,('Before breakfast and dinner',2,2,2)
,('Before breakfast and dinner PRN',0,0,2)
,('Before dialysis',0,0,1)
,('Before dinner',1,1,1)
,('Before meals and nightly PRN',0,0,4)
,('Code/trauma continuous med',1,1,1)
,('Code/trauma medication',1,1,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Continuous',1,1,4)
,('Continuous PRN',1,1,4)
,('Continuous TPN',1,1,1)
,('Cyclic TPN - see admin instructions',1,1,1)
,('Daily',1,1,1)
,('Daily at 1800',1,1,1)
,('Daily at 2000',1,1,1)
,('Daily before lunch',1,1,1)
,('Daily PRN',0,0,1)
,('Daily with breakfast',1,1,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Daily with dinner',1,1,1)
,('Daily with lunch',1,1,1)
,('Every 1 hour PRN',0,6,24)
,('Every 1 min',1,6,24)
,('Every 10 min',6,6,6)
,('Every 10 min PRN',0,0,144)
,('Every 12 hours',2,2,2)
,('Every 12 hours PRN',0,0,2)
,('Every 12 hours scheduled',2,2,2)
,('Every 14 days',0,0,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Every 15 min',4,4,92)
,('Every 15 min PRN',0,0,NULL)
,('every 2 hours',12,12,12)
,('Every 2 hours',12,12,12)
,('Every 2 hours PRN',0,0,12)
,('Every 2 hours while awake',8,8,12)
,('Every 2 minutes PRN',0,0,720)
,('Every 20 hours',1,1,1)
,('Every 20 min',3,3,72)
,('Every 20 min PRN',0,0,72)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Every 21 days',0,0,1)
,('Every 24 hours',1,1,1)
,('Every 24 hours scheduled',1,1,1)
,('Every 28 days',0,0,1)
,('Every 3 hours',8,8,8)
,('Every 3 hours PRN',0,0,8)
,('Every 30 days',0,0,1)
,('Every 30 min',2,2,2)
,('Every 30 min PRN',0,0,2)
,('Every 4 hours',6,6,6)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Every 4 hours PRN',0,0,6)
,('Every 4 hours scheduled',6,6,6)
,('Every 4 hours while awake',6,6,6)
,('Every 48 hours',0,0,1)
,('Every 48 hours PRN',0,0,1)
,('Every 5 hours',5,5,5)
,('Every 5 min',12,12,480)
,('Every 5 min PRN',0,0,480)
,('Every 6 hours',4,4,4)
,('Every 6 hours PRN',0,0,4)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Every 6 hours sched',4,4,4)
,('Every 6 hours scheduled',4,4,4)
,('Every 6 hours while awake',4,4,4)
,('Every 7 days',0,0,1)
,('Every 72 hours',0,0,1)
,('Every 8 hours',3,3,3)
,('Every 8 hours PRN',0,0,3)
,('Every 8 hours scheduled',3,3,3)
,('Every evening',1,1,1)
,('Every evening PRN',0,0,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Every hour',24,24,24)
,('Every Mon-Wed-Fri-Sun at 2000',0,0,1)
,('Every Mon-Wed-Fri at 0900',0,0,1)
,('Every Mon-Wed-Fri at 2000',0,0,1)
,('Every morning',1,1,1)
,('Every morning before breakfast',1,1,1)
,('Every other day',0,0,1)
,('Every shift',2,1,2)
,('Every Tue-Thur-Sat-Sun at 2000',0,0,1)
,('every Tue, Thu, Sat and Sun at 0900',0,0,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Every Tue,Thu,Sat at 2000',0,0,1)
,('Five times daily PRN',0,0,5)
,('Nightly',1,1,1)
,('Nightly - one time',1,1,1)
,('Nightly may repeat once PRN',1,1,2)
,('Nightly PRN',0,0,1)
,('On call',1,0,1)
,('Once',1,1,1)
,('Once - Respiratory',1,1,1)
,('Once at 2000',1,1,1)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('Once in dialysis',1,1,1)
,('Once PRN',0,0,1)
,('Once this morning',1,1,1)
,('Once tonight',1,1,1)
,('PRN',0,0,6)
,('Select Days at 2000',0,0,1)
,('Six times daily PRN',0,0,6)
,('Three times daily around food',3,3,3)
,('Three times weekly',0,0,3)
,('Titrated',1,1,4)
;
INSERT INTO sig_mapping (sig,expected,min,max) VALUES 
('User specified',0,0,4)
,('Weekly',0,0,1)
,('With meals and nightly',4,4,4)
;