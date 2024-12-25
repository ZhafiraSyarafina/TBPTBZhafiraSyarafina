// seeders/20241220-demo-all-data.js
'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {
    // 1. Seed Members
    const members = [];
    for (let i = 1; i <= 50; i++) {
      members.push({
        nim: `2024${String(i).padStart(4, '0')}`,
        name: `Mahasiswa ${i}`,
        phoneNumber: `08123${String(Math.floor(Math.random() * 10000000)).padStart(8, '0')}`,
        email: `mahasiswa${i}@university.ac.id`,
        createdAt: new Date(),
        updatedAt: new Date()
      });
    }
    await queryInterface.bulkInsert('Members', members);

    // 2. Seed Groups
    const groups = [];
    for (let i = 1; i <= 15; i++) {
      groups.push({
        name: `Kelompok KP ${i}`,
        createdAt: new Date(),
        updatedAt: new Date()
      });
    }
    await queryInterface.bulkInsert('Groups', groups);

    // 3. Seed GroupMembers (3-4 members per group)
    const groupMembers = [];
    let memberCounter = 1;
    for (let groupId = 1; groupId <= 15; groupId++) {
      const membersCount = Math.floor(Math.random() * 2) + 3; // 3 or 4 members
      for (let j = 0; j < membersCount; j++) {
        if (memberCounter <= 50) { // Make sure we don't exceed available members
          groupMembers.push({
            groupId: groupId,
            memberId: memberCounter,
            createdAt: new Date(),
            updatedAt: new Date()
          });
          memberCounter++;
        }
      }
    }
    await queryInterface.bulkInsert('GroupMembers', groupMembers);

    // 4. Seed KPRequests
    const companies = [
      'PT Teknologi Maju',
      'CV Digital Solusi',
      'PT Informatika Global',
      'PT Sistem Andal',
      'CV Tech Solutions',
      'PT Data Analytics',
      'PT Cloud Computing',
      'CV Mobile Solutions',
      'PT Web Developer',
      'PT Software House'
    ];

    const statuses = ['Pending', 'Approved', 'Rejected'];
    const kpRequests = [];

    for (let groupId = 1; groupId <= 15; groupId++) {
      const requestsPerGroup = Math.floor(Math.random() * 3) + 1; // 1-3 requests per group
      
      for (let i = 0; i < requestsPerGroup; i++) {
        const startDate = new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1);
        const endDate = new Date(startDate);
        endDate.setMonth(endDate.getMonth() + 3);

        kpRequests.push({
          groupId: groupId,
          company: companies[Math.floor(Math.random() * companies.length)],
          startDate: startDate.toISOString().split('T')[0],
          endDate: endDate.toISOString().split('T')[0],
          proposalUrl: `https://storage.com/proposals/kelompok${groupId}_${i + 1}.pdf`,
          status: statuses[Math.floor(Math.random() * statuses.length)],
          createdAt: new Date(),
          updatedAt: new Date()
        });
      }
    }
    await queryInterface.bulkInsert('KPRequests', kpRequests);

    // 5. Seed Replies (only for non-pending requests)
    const replies = [];
    const nonPendingRequests = kpRequests.filter(kr => kr.status !== 'Pending');

    for (let request of nonPendingRequests) {
      replies.push({
        idKP: request.id,
        responseLetterUrl: `https://storage.com/responses/request${request.id}_response.pdf`,
        createdAt: new Date(),
        updatedAt: new Date()
      });
    }
    await queryInterface.bulkInsert('Replies', replies);

    // 6. Seed Infos
    const infos = [
      {
        title: 'Pengumuman Pendaftaran KP 2024',
        description: 'Pendaftaran Kerja Praktek untuk periode 2024 telah dibuka. Silakan mengajukan proposal melalui sistem.',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        title: 'Deadline Pengumpulan Laporan KP',
        description: 'Batas akhir pengumpulan laporan KP adalah 2 minggu setelah masa KP berakhir.',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        title: 'Panduan Penulisan Proposal KP',
        description: 'Silakan download template proposal KP terbaru di link berikut...',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        title: 'Daftar Perusahaan Mitra KP',
        description: 'Berikut adalah daftar perusahaan yang telah menjalin kerjasama untuk program KP...',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        title: 'Syarat dan Ketentuan KP',
        description: 'Mahasiswa wajib memenuhi minimal 100 SKS sebelum mengajukan KP...',
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];
    await queryInterface.bulkInsert('Infos', infos);
  },

  down: async (queryInterface, Sequelize) => {
    // Delete in reverse order to avoid foreign key constraints
    await queryInterface.bulkDelete('Infos', null, {});
    await queryInterface.bulkDelete('Replies', null, {});
    await queryInterface.bulkDelete('KPRequests', null, {});
    await queryInterface.bulkDelete('GroupMembers', null, {});
    await queryInterface.bulkDelete('Groups', null, {});
    await queryInterface.bulkDelete('Members', null, {});
  }
};